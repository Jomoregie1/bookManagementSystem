package com.bookstore.managementsystem.repo;

import com.bookstore.managementsystem.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

    @Query(value = "SELECT COUNT(isbn) > 0 FROM book WHERE isbn = :isbn", nativeQuery = true)
    Long countByIsbn(@Param("isbn") long isbn);

    @Query("SELECT b FROM Book b WHERE b.author.id = :author_id")
    List<Book>findAllByAuthor(@Param("author_id") long id);

    @Query("SELECT b FROM Book b WHERE price BETWEEN :startPrice AND :endPrice")
    List<Book> findByPriceBetween(@Param("startPrice") double startPrice, @Param("endPrice") double endPrice);

    @Query("SELECT b FROM Book b WHERE title = :title")
    Optional<Book>findByTitle(@Param("title") String title);


}
