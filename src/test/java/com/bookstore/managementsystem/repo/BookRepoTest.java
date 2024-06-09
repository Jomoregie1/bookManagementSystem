package com.bookstore.managementsystem.repo;

import com.bookstore.managementsystem.entity.Author;
import com.bookstore.managementsystem.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepoTest {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private TestEntityManager testEntityManager;
    private Book book;
    private Author author;

    @BeforeEach
    void setUp() {

        this.author = Author.builder()
                .name("J.K.Rowling")
                .build();

        this.author = testEntityManager.persistAndFlush(this.author);


        this.book = Book.builder()
                .isbn(12345L)
                .author(this.author)
                .title("Harry porter")
                .publicationDate(LocalDate.of(2020, 10, 10))
                .price(21.5)
                .build();

        this.book = testEntityManager.persistAndFlush(this.book);

        testEntityManager.clear();
    }

    @Test
    public void testExistsByIsbn() {
        Long isbnCountTest = bookRepo.countByIsbn(book.getIsbn());
        assertTrue(isbnCountTest > 0);

    }

    @Test
    public void testFindAllByAuthor() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> booksByAuthor = bookRepo.findAllByAuthor(this.author.getId(), pageable);
        assertEquals(1, booksByAuthor.getTotalPages());
        assertEquals(1, booksByAuthor.getTotalElements());
    }

    @Test
    public void testfindByPriceBetween() {
        double lowerBound = 20.5;
        double upperBound = 22.5;

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> booksBetweenGivenPrice = bookRepo.findByPriceBetween(lowerBound, upperBound,pageable);
        assertEquals(1, booksBetweenGivenPrice.getTotalElements());
        assertEquals(1, booksBetweenGivenPrice.getTotalPages());
    }
}