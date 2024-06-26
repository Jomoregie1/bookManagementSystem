package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.AlreadyExistsError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.entity.Author;
import com.bookstore.managementsystem.entity.Book;
import com.bookstore.managementsystem.repo.AuthorRepo;
import com.bookstore.managementsystem.repo.BookRepo;
import com.bookstore.managementsystem.utils.MapConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Qualifier("BookService")
@Slf4j
public class BookServiceImpl implements BookService{

    private BookRepo bookRepo;
    private MapConvertor mapper;
    private AuthorRepo authorRepo;

    @Autowired
    BookServiceImpl(BookRepo bookRepo, MapConvertor mapConvertor, AuthorRepo authorRepo) {
        this.bookRepo = bookRepo;
        this.mapper = mapConvertor;
        this.authorRepo = authorRepo;
    }

    @Override
    public ResponseEntity<BookDto> createBook(BookDto bookDto) throws AlreadyExistsError {
        String logMessage;

        Author author = authorRepo.findAuthorByName(bookDto.getAuthorName())
                .orElseGet(() -> {
                    Author newAuthor = Author.builder()
                            .name(bookDto.getAuthorName())
                            .build();
                    return authorRepo.save(newAuthor);
                });

        long isbnCount = bookRepo.countByIsbn(bookDto.getIsbn());
        if (isbnCount > 0) {
            logMessage = String.format("Error: Book with ISBN - %d, already exists.", bookDto.getIsbn());
            log.error(logMessage);
            throw new AlreadyExistsError("The book you are trying to create has been found in our system.");
        }


        Book book = mapper.bookDtoToBook(bookDto);
        book.setAuthor(author);

        author.addBook(book);


        logMessage = String.format("Book with ISBN - %d, created.", bookDto.getIsbn());
        log.info(logMessage);
        bookRepo.save(book);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookDto);
    }

    @Override
    public ResponseEntity<List<BookDto>> getAllBooks(int page, int size) throws NotFoundError {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepo.findAll(pageable);

        if (!books.hasContent()) {
            throw new NotFoundError("No Books Found");
        }

        List<BookDto> responseBookDtos = books.getContent().stream().map(mapper::bookToBookDto).toList();
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

        Book convertedBook = mapper.bookDtoToBook(bookDto);
        bookRepo.save(convertedBook);

        return ResponseEntity.status(HttpStatus.OK).body(bookDto);
    }

    @Override
    public ResponseEntity<Void> deleteBook(Long id) throws NotFoundError{
        Book book = bookRepo.findById(id).orElseThrow(() -> new NotFoundError("Book with ID: " + id + " has not been found."));

        // TODO need to test that this successfully happens(possibly during integration tests.)
        Author author = book.getAuthor();
        if (author != null) {
            author.removeBook(book);
            authorRepo.save(author);
        }

        bookRepo.deleteById(id);
        return ResponseEntity.status(200).build();
    }

    @Override
    public ResponseEntity<List<BookDto>> getBooksByAuthor(Long id, int page, int size) throws NotFoundError {
        Pageable pageable = PageRequest.of(page,size);
        Page<Book> bookList = bookRepo.findAllByAuthor(id,pageable);
        if (!bookList.hasContent()) {
            throw new NotFoundError("Author with ID: " +id+ " has not been found.");
        }

        List<BookDto> bookDtoList = bookList.stream().map(mapper::bookToBookDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(bookDtoList);

    }

    @Override
    public ResponseEntity<List<BookDto>> getBookWithInPriceRange(double startOfRange, double endOfRange,
                                                                 int page, int size) throws NotFoundError {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookList = bookRepo.findByPriceBetween(startOfRange, endOfRange,pageable);
        if (!bookList.hasContent()) {
            throw new NotFoundError("Books with price starting from "
                    +startOfRange+ " and ending at "+endOfRange+", has not been found.");
        }

        List<BookDto> bookDtoList = bookList.getContent().stream().map(mapper::bookToBookDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(bookDtoList);
        }
}
