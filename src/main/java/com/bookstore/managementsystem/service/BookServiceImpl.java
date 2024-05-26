package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.dto.BookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{


    @Override
    public ResponseEntity<BookDto> createBook(BookDto book) {
        return null;
    }

    @Override
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return null;
    }

    @Override
    public ResponseEntity<BookDto> getBook(long id) {
        return null;
    }
}
