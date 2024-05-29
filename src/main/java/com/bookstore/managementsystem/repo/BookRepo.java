package com.bookstore.managementsystem.repo;

import com.bookstore.managementsystem.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Long> {
}
