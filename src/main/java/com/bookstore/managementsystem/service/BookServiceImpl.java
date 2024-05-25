package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.dto.BookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService{


    @Override
    public ResponseEntity<BookDto> createBook(BookDto book) {
        return null;
    }
}
