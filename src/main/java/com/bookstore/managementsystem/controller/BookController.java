package com.bookstore.managementsystem.controller;

import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @PostMapping()
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto){
        return bookService.createBook(bookDto);
    }
}
