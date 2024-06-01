package com.bookstore.managementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "author_name")
    private String name;

    @OneToMany(mappedBy = "author", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<Book> books;

}
