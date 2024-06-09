package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.AlreadyExistsError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
import com.bookstore.managementsystem.dto.BookDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface BookService {

    ResponseEntity<BookDto> createBook(BookDto book) throws AlreadyExistsError;

    ResponseEntity<List<BookDto>> getAllBooks(int page, int size) throws NotFoundError;

    ResponseEntity<BookDto> getBook(long id) throws NotFoundError;

    ResponseEntity<BookDto> updateBook(Long id, BookDto bookDto) throws NotFoundError;

    ResponseEntity<Void>deleteBook(Long id) throws NotFoundError;

    ResponseEntity<List<BookDto>> getBooksByAuthor(Long id, int page, int size) throws NotFoundError;

    ResponseEntity<List<BookDto>> getBookWithInPriceRange(double startOfRange, double endOfRange, int page, int size) throws NotFoundError;
 }
