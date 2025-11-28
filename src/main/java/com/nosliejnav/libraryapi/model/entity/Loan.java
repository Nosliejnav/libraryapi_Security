package com.nosliejnav.libraryapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
// import org.hibernate.annotations.ManyToAny; // Esta importação não é mais necessária

@Builder
@Entity
@Table
// Adicione as anotações @Data, @AllArgsConstructor e @NoArgsConstructor para simplificar,
// ou mantenha os construtores e getters/setters manuais, como você fez:
@Data // Adicionado para simplificar getters/setters/equals/hashCode/toString
@NoArgsConstructor // Adicionado para construtor sem argumentos
@AllArgsConstructor // Adicionado para construtor com todos os argumentos
public class Loan {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String customer;

    // CORREÇÃO: Usar @ManyToOne, pois muitos Loans (Empréstimos)
    // apontam para um único Book (Livro).
    @ManyToOne
    @JoinColumn(name = "id_book") // Define a chave estrangeira na tabela Loan
    private Book book;

    @Column
    private LocalDate loanDate;

    @Column
    private Boolean returned;

    /*
     * Mantenho os construtores e Getters/Setters manuais caso você não queira usar @Data/@NoArgsConstructor/@AllArgsConstructor.
     * Se usar @Data/@NoArgsConstructor/@AllArgsConstructor, você pode remover o código abaixo.
     */

//     public Loan(Long id, String customer, Book book, LocalDate loanDate, Boolean returned) {
//         this.id = id;
//         this.customer = customer;
//         this.book = book;
//         this.loanDate = loanDate;
//         this.returned = returned;
//     }
//
//     public Loan() {
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
//     public String getCustomer() {
//         return customer;
//     }
//
//     public void setCustomer(String customer) {
//         this.customer = customer;
//     }
//
//     public Book getBook() {
//         return book;
//     }
//
//     public void setBook(Book book) {
//         this.book = book;
//     }
//
//     public LocalDate getLoanDate() {
//         return loanDate;
//     }
//
//     public void setLoanDate(LocalDate loanDate) {
//         this.loanDate = loanDate;
//     }
//
//     public Boolean getReturned() {
//         return returned;
//     }
//
//     public void setReturned(Boolean returned) {
//         this.returned = returned;
//     }
}