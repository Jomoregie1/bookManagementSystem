package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.dto.BookDto;
import org.springframework.http.ResponseEntity;

public interface BookService {


    ResponseEntity<BookDto> createBook(BookDto book);


}
