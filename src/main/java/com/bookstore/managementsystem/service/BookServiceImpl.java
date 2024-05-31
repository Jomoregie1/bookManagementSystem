package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.BookExistsError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.entity.Book;
import com.bookstore.managementsystem.repo.BookRepo;
import com.bookstore.managementsystem.utils.MapConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Qualifier("BookService")
@Slf4j
public class BookServiceImpl implements BookService{

    private BookRepo bookRepo;
    private MapConvertor mapper;

    @Autowired
    BookServiceImpl(BookRepo bookRepo, MapConvertor mapConvertor) {
        this.bookRepo = bookRepo;
        this.mapper = mapConvertor;
    }

    @Override
    public ResponseEntity<BookDto> createBook(BookDto book) throws BookExistsError {
        String logMessage;
        if (bookRepo.existsByIsbn(book.getIsbn())) {
            logMessage = String.format("Error: Book with ISBN - %d, already exists.", book.getIsbn());
            log.error(logMessage);
            throw new BookExistsError("The book you are trying to create has been found in our system.");
        }

        Book current_book = mapper.BookDtoToBook(book);
        logMessage = String.format("Book with ISBN - %d, created.", book.getIsbn());
        log.info(logMessage);
        bookRepo.save(current_book);

        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @Override
    public ResponseEntity<List<BookDto>> getAllBooks() throws NotFoundError {
        List<Book> books = bookRepo.findAll();
        if (books.isEmpty()) {
            throw new NotFoundError("No Books Found");
        }

        List<BookDto> responseBookDtos = books.stream().map(mapper::bookToBookDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(responseBookDtos);
    }

    @Override
    public ResponseEntity<BookDto> getBook(long id) throws NotFoundError{
        Optional<Book> bookOptional = bookRepo.findById(id);
        if (bookOptional.isEmpty()) {
            throw new NotFoundError("The book with Id:" + id + ",Not Found.");
        }
        BookDto responseBook = mapper.bookToBookDto(bookOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(responseBook);

    }

    @Override
    public ResponseEntity<BookDto> updateBook(Long id, BookDto bookDto) throws NotFoundError{
        boolean exists = bookRepo.existsById(id);
        if (!exists) {
            throw new NotFoundError("Book with ID: "+id+" has not been found.");
        }

        Book convertedBook = mapper.BookDtoToBook(bookDto);
        bookRepo.save(convertedBook);

        return ResponseEntity.status(HttpStatus.OK).body(bookDto);
    }

    @Override
    public ResponseEntity<Void> deleteBook(Long id) throws NotFoundError{
        boolean exists = bookRepo.existsById(id);
        if (!exists) {
            throw new NotFoundError("Book with ID: " +id+ " has not been found.");
        }
        bookRepo.deleteById(id);
        return ResponseEntity.status(200).build();
    }

    @Override
    public ResponseEntity<List<BookDto>> getBooksByAuthor(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<BookDto>> getBookWithInPriceRange(double startOfRange, double endOfRange) {
        return null;
    }
}
