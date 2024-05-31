package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.BookExistsError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
import com.bookstore.managementsystem.dto.BookDto;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface BookService {

    ResponseEntity<BookDto> createBook(BookDto book) throws BookExistsError;

    ResponseEntity<List<BookDto>> getAllBooks() throws NotFoundError;

    ResponseEntity<BookDto> getBook(long id) throws NotFoundError;

    ResponseEntity<BookDto> updateBook(Long id, BookDto bookDto) throws NotFoundError;

    ResponseEntity<Void>deleteBook(Long id) throws NotFoundError;

    ResponseEntity<List<BookDto>> getBooksByAuthor(Long id) throws NotFoundError;

    ResponseEntity<List<BookDto>> getBookWithInPriceRange(double startOfRange, double endOfRange);
 }
