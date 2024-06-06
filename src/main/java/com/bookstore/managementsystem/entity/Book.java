package com.bookstore.managementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private long isbn;
    private LocalDate publicationDate;
    private double price;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany(mappedBy = "books")
    private Set<Order> orders;

    public void addOrder(Order order) {
        this.orders.add(order);
        order.getBooks().add(this);
    }

    public void removeOrder(Order order) {
        this.orders.remove(order);
        order.getBooks().remove(this);
    }


}
