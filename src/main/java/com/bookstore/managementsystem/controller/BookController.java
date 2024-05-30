package com.bookstore.managementsystem.controller;

import com.bookstore.managementsystem.customerrors.BookExistsError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping()
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) throws BookExistsError {
        return bookService.createBook(bookDto);
    }

    @GetMapping()
    public ResponseEntity<List<BookDto>> getAllBooks() throws NotFoundError {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable("id") Long id) {
        return bookService.getBook(id);
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<List<BookDto>> getBooksByAuthor (@PathVariable("id") Long id) {
        return bookService.getBooksByAuthor(id);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<BookDto>> getBooksByPriceRange(@RequestParam double startOfRange, @RequestParam double endOfRange) {
        return bookService.getBookWithInPriceRange(startOfRange,endOfRange);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable("id") Long id, BookDto bookDto) {
        return bookService.updateBook(id, bookDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        return bookService.deleteBook(id);
    }
}
