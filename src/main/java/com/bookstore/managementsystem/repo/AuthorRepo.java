package com.bookstore.managementsystem.repo;

import com.bookstore.managementsystem.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepo extends JpaRepository<Author, Long> {
}
