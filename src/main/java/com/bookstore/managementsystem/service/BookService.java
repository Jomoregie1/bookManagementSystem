package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.dto.BookDto;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface BookService {


    ResponseEntity<BookDto> createBook(BookDto book);


    ResponseEntity<List<BookDto>> getAllBooks();

    ResponseEntity<BookDto> getBook(long id);

    ResponseEntity<BookDto> updateBook(Long id, BookDto bookDto);
 }
