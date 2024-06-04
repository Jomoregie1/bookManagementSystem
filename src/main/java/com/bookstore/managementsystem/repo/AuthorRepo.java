package com.bookstore.managementsystem.repo;

import com.bookstore.managementsystem.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a Where name = :name")
    Optional<Author> findAuthorByName(@Param("name") String name);
}
