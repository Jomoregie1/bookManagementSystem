package com.bookstore.managementsystem.controller;

import com.bookstore.managementsystem.customerrors.AlreadyExistsError;
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
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) throws AlreadyExistsError {
        return bookService.createBook(bookDto);
    }

    @GetMapping()
    public ResponseEntity<List<BookDto>> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) throws NotFoundError {
        return bookService.getAllBooks(page,size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable("id") Long id) throws NotFoundError {
        return bookService.getBook(id);
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<List<BookDto>> getBooksByAuthor (@PathVariable("id") Long id, @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) throws NotFoundError {
        return bookService.getBooksByAuthor(id,page,size);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<BookDto>> getBooksByPriceRange(@RequestParam double startOfRange, @RequestParam double endOfRange,
                                                              @RequestParam (defaultValue = "0") int page
            , @RequestParam(defaultValue = "10") int size) throws NotFoundError {
        return bookService.getBookWithInPriceRange(startOfRange,endOfRange,page,size);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable("id") Long id, BookDto bookDto) throws NotFoundError{
        return bookService.updateBook(id, bookDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) throws NotFoundError {
        return bookService.deleteBook(id);
    }
}
