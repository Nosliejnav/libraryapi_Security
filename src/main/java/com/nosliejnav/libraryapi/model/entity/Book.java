package com.nosliejnav.libraryapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Entity
@Table
// Adicione as anotações @Data, @AllArgsConstructor e @NoArgsConstructor para simplificar,
// ou mantenha os construtores e getters/setters manuais, como você fez:
@Data // Adicionado para simplificar getters/setters/equals/hashCode/toString
@NoArgsConstructor // Adicionado para construtor sem argumentos
@AllArgsConstructor // Adicionado para construtor com todos os argumentos
public class Book {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String isbn;

    // Um Book (Livro) pode ter muitos Loans (Empréstimos).
    // mappedBy="book" indica que a associação é mapeada pela propriedade "book" na entidade Loan.
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Loan> loans;

    /*
     * Mantenho os construtores e Getters/Setters manuais caso você não queira usar @Data/@NoArgsConstructor/@AllArgsConstructor.
     * Se usar @Data/@NoArgsConstructor/@AllArgsConstructor, você pode remover o código abaixo.
     */

//     public Book(Long id, String title, String author, String isbn, List<Loan> loans) {
//         this.id = id;
//         this.title = title;
//         this.author = author;
//         this.isbn = isbn;
//         this.loans = loans;
//     }
//
//     public Book() {
//     }
//
//     public Long getId() {
//         return id;
//     }
//
//     public void setId(Long id) {
//         this.id = id;
//     }
//
//     public String getTitle() {
//         return title;
//     }
//
//     public void setTitle(String title) {
//         this.title = title;
//     }
//
//     public String getAuthor() {
//         return author;
//     }
//
//     public void setAuthor(String author) {
//         this.author = author;
//     }
//
//     public String getIsbn() {
//         return isbn;
//     }
//
//     public void setIsbn(String isbn) {
//         this.isbn = isbn;
//     }
//
//     public List<Loan> getLoans() {
//         return loans;
//     }
//
//     public void setLoans(List<Loan> loans) {
//         this.loans = loans;
//     }
}