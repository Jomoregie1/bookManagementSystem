package com.bookstore.managementsystem.repo;

import com.bookstore.managementsystem.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

    @Query("Select count(isbn) > 0 From Book Where isbn = :isbn")
    boolean existsByIsbn(@Param("isbn") long isbn);

    @Query("from Book where author_id = :author_id")
    List<Book>findAllByAuthor(@Param("author_id") long id);


}
