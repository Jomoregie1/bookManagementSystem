package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.BookExistsError;
import com.bookstore.managementsystem.dto.BookDto;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface BookService {

    ResponseEntity<BookDto> createBook(BookDto book) throws BookExistsError;

    ResponseEntity<List<BookDto>> getAllBooks();

    ResponseEntity<BookDto> getBook(long id);

    ResponseEntity<BookDto> updateBook(Long id, BookDto bookDto);

    ResponseEntity<Void>deleteBook(Long id);

    ResponseEntity<List<BookDto>> getBooksByAuthor(Long id);

    ResponseEntity<List<BookDto>> getBookWithInPriceRange(double startOfRange, double endOfRange);
 }
