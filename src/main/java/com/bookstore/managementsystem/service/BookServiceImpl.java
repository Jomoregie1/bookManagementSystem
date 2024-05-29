package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.BookExistsError;
import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.entity.Book;
import com.bookstore.managementsystem.repo.BookRepo;
import com.bookstore.managementsystem.utils.MapConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("BookService")
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

        if (bookRepo.existsByIsbn(book.getIsbn())) {
            throw new BookExistsError("The book you are trying to create has been found in our system.");
        }

        Book current_book = mapper.BookDtoToBook(book);
        bookRepo.save(current_book);

        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @Override
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return null;
    }

    @Override
    public ResponseEntity<BookDto> getBook(long id) {
        return null;
    }

    @Override
    public ResponseEntity<BookDto> updateBook(Long id, BookDto bookDto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteBook(Long id) {
        return null;
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
