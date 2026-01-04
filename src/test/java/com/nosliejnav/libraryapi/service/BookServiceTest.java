package com.nosliejnav.libraryapi.service;

import com.nosliejnav.libraryapi.exception.BusinessException;
import com.nosliejnav.libraryapi.model.entity.Book;
import com.nosliejnav.libraryapi.model.repository.BookRepository;
import com.nosliejnav.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService bookService;

//    @MockitoBean
    BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        this.bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    @DisplayName("Deve salvar um livro.")
    public void saveBookTest() {

        // cenario
        Book book = createValidBook();
        Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(bookRepository.save(book)).thenReturn(
                Book.builder().id(1l)
                        .isbn("123")
                        .author("Fulano")
                        .title("As aventuras").build());

        // execucao
        Book savedBook = bookService.save(book);

        // verificacao
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
    }

    private static Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado.")
    public void shouldNotSaveABookWithDuplicatedIESN() {

        // cenario
        Book book = createValidBook();
        Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        // execucao
        Throwable exception = Assertions.catchThrowable(() -> bookService.save(book));

        // verificacoes
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado.");

        Mockito.verify(bookRepository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve obter um livro por Id.")
    public void getByIdTest() {
        Long id = 1l;
        Book book = createValidBook();
        book.setId(id);

        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        // execucao
        Optional<Book> foundBook = bookService.getById(id);

        // verificacoes
        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro por Id quando ele não existe na base.")
    public void bookNotFoundByIdTest() {
        Long id = 1l;
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // execucao
        Optional<Book> book = bookService.getById(id);

        // verificacoes
        assertThat(book.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve deletar um livro.")
    public void deleteBookTest() {
        Book book = Book.builder().id(1l).build();

        // execucao
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> bookService.delete(book));

        // verificacoes
        Mockito.verify(bookRepository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar deletar um livro inexistente.")
    public void deleteInvalidBookTest() {

        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> bookService.delete(book));

        Mockito.verify(bookRepository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar deletar um livro inexistente.")
    public void updateInvalidBookTest() {
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> bookService.update(book));

        Mockito.verify(bookRepository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro.")
    public void updateBookTest() {
        // cenario
        long id = 1l;

        // livro a atualizar
        Book updatingBook = Book.builder().id(id).build();

        // simulação
        Book updatedBook = createValidBook();
        updatedBook.setId(id);
        Mockito.when(bookRepository.save(updatingBook)).thenReturn(updatedBook);

        // execução
        Book book = bookService.update(updatingBook);

        // verificações
        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());

    }

    @Test
    @DisplayName("Deve filtrar livros pelas propriedades.")
    public void findBookTest() {

        // cenario
        Book book = createValidBook();

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Book> lista = Arrays.asList(book);
        Page<Book> page = new PageImpl<Book>(lista, pageRequest, 1);
        when(bookRepository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        // execucao
        Page<Book> result = bookService.find(book, pageRequest);

        // verificacoes
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("deve obter um livro pelo isbn ")
    public void getBookByIsbnTest() {
        String isbn = "1230";

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(Book.builder().id(1l).isbn(isbn).build()));

        Optional<Book> book = bookService.getBookByIsbn(isbn);

        assertThat(book.isPresent()).isTrue();
        assertThat(book.get().getId()).isEqualTo(1l);
        assertThat(book.get().getIsbn()).isEqualTo(isbn);

        verify(bookRepository, times(1)).findByIsbn(isbn);
    }

}
