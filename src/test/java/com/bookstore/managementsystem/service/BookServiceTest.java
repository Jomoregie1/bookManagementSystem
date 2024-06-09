package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.AlreadyExistsError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.entity.Author;
import com.bookstore.managementsystem.entity.Book;
import com.bookstore.managementsystem.repo.AuthorRepo;
import com.bookstore.managementsystem.repo.BookRepo;
import com.bookstore.managementsystem.utils.MapConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private MapConvertor mapConvertor;

    @Mock
    private AuthorRepo authorRepo;

    @Mock
    private BookRepo bookRepo;
    private BookDto bookDto;
    private Book book;
    private Author author;
    private Page<Book> pageList;
    private int pageSize;
    private int contentSize;

    @BeforeEach
    void setUp() {

        this.pageSize = 1;
        this.contentSize = 10;
        Set<Book> books = new HashSet<>();

        this.author = Author.builder()
                .id(1L)
                .name("J.K.Rowling")
                .books(books)
                .build();


        this.bookDto = BookDto.builder()
                .price(10)
                .isbn(12345678)
                .title("Random title")
                .publicationDate(LocalDate.of(2020,1,10))
                .authorName(this.author.getName())
                .build();

        this.book = Book.builder()
                .price(10)
                .isbn(12345678)
                .title("Random title")
                .publicationDate(LocalDate.of(2020,1,10))
                .id(1)
                .author(this.author)
                .build();


        List<Book> booksList = List.of(book, book, book);
        this.pageList =  new PageImpl<>(booksList, PageRequest.of(contentSize, pageSize), booksList.size());


    }

    @Test
    @DisplayName("Test create book in service layer.")
    public void testCreateBook_WhenNewBookObjectCreated_ReturnCreatedBookObjectAndOkStatus() throws AlreadyExistsError {
        // Arrange
        when(mapConvertor.bookDtoToBook(any(BookDto.class)))
                .thenReturn(this.book);
        when(bookRepo.countByIsbn(any(Long.class))).thenReturn(0L);
        when(authorRepo.findAuthorByName(any(String.class))).thenReturn(Optional.of(this.author));

        //Act
        ResponseEntity<BookDto> response = bookService.createBook(bookDto);
        int statusCode = response.getStatusCode().value();
        BookDto responseBody = response.getBody();

        //Assert
        assertEquals(bookDto, responseBody);
        assertEquals(201, statusCode);

    }


    @Test
    public void testCreateBook_WhenBookExistsErrorThrown_ThenTestExceptionThrow() throws AlreadyExistsError {
        // Arrange
        when(authorRepo.findAuthorByName(any(String.class))).thenReturn(Optional.of(this.author));
        when(bookRepo.countByIsbn(any(Long.class)))
                .thenReturn(1L);

        //Act & Assert
        AlreadyExistsError thrown = assertThrows(AlreadyExistsError.class, () -> {bookService.createBook(bookDto);});
        assertEquals("The book you are trying to create has been found in our system.", thrown.getMessage());


    }

    @Test
    public void testGetAllBooks_ThenReturnAListOfBookDtos() throws NotFoundError {
        //Arrange
        when(bookRepo.findAll(any(Pageable.class))).thenReturn(this.pageList);
        when(mapConvertor.bookToBookDto(any(Book.class))).thenReturn(bookDto);

        // Act
        ResponseEntity<List<BookDto>> response = bookService.getAllBooks(this.pageSize, this.contentSize);

        //Assert
        int statusCode = response.getStatusCode().value();
        List<BookDto> bookDtoList = response.getBody();

        assertEquals(200,statusCode);
        assertEquals(3, bookDtoList.size());

    }

    @Test
    public void testGetAllBooks_WhenFindAllReturnsEmptyList_ThenThrowNotFoundError() throws NotFoundError{
        // Arrange
        when(bookRepo.findAll(any(Pageable.class))).thenReturn(Page.empty());

        //Act & Assert
        NotFoundError thrown = assertThrows(NotFoundError.class, () -> {
            bookService.getAllBooks(this.pageSize,this.contentSize);
        });
        assertEquals("No Books Found", thrown.getMessage());
    }

    @Test
    public void testGetBook_WhenBookIdPassed_ThenReturnBookFound() throws NotFoundError{
        when(bookRepo.findById(any(Long.class))).thenReturn(Optional.of(this.book));
        when(mapConvertor.bookToBookDto(any(Book.class))).thenReturn(this.bookDto);

        long testId = 1L;
        ResponseEntity<BookDto> response = bookService.getBook(testId);
        int statusCode = response.getStatusCode().value();
        BookDto testBookDto = response.getBody();

        assertEquals(200, statusCode);
        assertEquals(this.bookDto, testBookDto);

    }

    @Test
    public void testGetBook_WhenBookIdNotFound_ThenThrowNotFoundException() throws NotFoundError{
        when(bookRepo.findById(any(Long.class))).thenReturn(Optional.empty());

        long testId = 1L;
        NotFoundError thrown = assertThrows(NotFoundError.class, () -> {bookService.getBook(testId);});
        assertEquals("The book with Id:1,Not Found.", thrown.getMessage());
    }

    @Test
    public void testUpdateBook_WhenUpdatingBook_ThenReturnSucess() throws NotFoundError {
        when(bookRepo.existsById(any(Long.class)))
                .thenReturn(true);
        when(mapConvertor.bookDtoToBook(any(BookDto.class))).thenReturn(this.book);

        long bookId = 1L;

        ResponseEntity<BookDto> response = bookService.updateBook(bookId,this.bookDto);
        int statusCode = response.getStatusCode().value();
        BookDto testBookDto = response.getBody();

        assertEquals(200,statusCode);
        assertEquals(this.bookDto, testBookDto);


    }

    @Test
    public void testUpdateBook_WhenUpdatingBookWithInvalidId_ThenAssertNotFoundErrorFound() throws NotFoundError{
        when(bookRepo.existsById(any(Long.class))).thenReturn(false);
        Long testId = 1L;

        NotFoundError thrown = assertThrows(NotFoundError.class, () -> {bookService.updateBook(testId, bookDto);});
        assertEquals("Book with ID: 1 has not been found.",thrown.getMessage());
    }

    @Test
    public void testDeleteBook_WhenBookIdProvided_ThenReturnOkStatus() throws NotFoundError{
        when(bookRepo.findById(any(Long.class))).thenReturn(Optional.of(this.book));
        long bookId = 1L;
        ResponseEntity<Void> response = this.bookService.deleteBook(bookId);
        int statusCode = response.getStatusCode().value();
        assertEquals(200, statusCode);

    }

    @Test
    public void testDeleteBook_WhenBookIdProvidedDoesNotExist_ThenThrowNotFoundError() {
        when(bookRepo.findById(any(Long.class))).thenReturn(Optional.empty());
        long testId = 1L;
        NotFoundError thrown = assertThrows(NotFoundError.class, () -> {bookService.deleteBook(testId);});
        assertEquals("Book with ID: 1 has not been found.",thrown.getMessage());
    }

    @Test
    public void testGetBookByAuthor_WhenAuthorIdProvided_ThenReturnAListOfBooks() throws NotFoundError {
        when(bookRepo.findAllByAuthor(any(Long.class), any(Pageable.class))).thenReturn(this.pageList);
        when(mapConvertor.bookToBookDto(any(Book.class))).thenReturn(this.bookDto);
        long testAuthorId = 1L;


        ResponseEntity<List<BookDto>> response = bookService.getBooksByAuthor(testAuthorId,this.pageSize, this.contentSize);
        int statusCode = response.getStatusCode().value();
        List<BookDto> bookDtoList = response.getBody();

        assertEquals(200, statusCode);
        assertEquals(3, bookDtoList.size());

    }


    @Test
    public void testGetBookByAuthor_WhenAuthorIdProvidedIsInvalid_ThenThrowNotFoundError() {
        when(bookRepo.findAllByAuthor(any(Long.class),any(Pageable.class))).thenReturn(Page.empty());

        long testAuthorId = 1L;
        NotFoundError thrown = assertThrows(NotFoundError.class, () -> {bookService.getBooksByAuthor(testAuthorId,
                this.pageSize, this.contentSize);});

        assertEquals("Author with ID: 1 has not been found.",thrown.getMessage());

    }

    @Test
    public void testGetBookWithInPriceRange_WhenStartPriceAndEndPriceProvided_ThenReturnBooksFound() throws NotFoundError {
        when(bookRepo.findByPriceBetween(any(Double.class), any(Double.class), any(Pageable.class)))
                .thenReturn(this.pageList);
        when(mapConvertor.bookToBookDto(any(Book.class))).thenReturn(bookDto);

        double startPrice = 10.5;
        double endPrice = 15.5;
        ResponseEntity<List<BookDto>> response = bookService.getBookWithInPriceRange(startPrice, endPrice,
                this.pageSize, this.contentSize);

        int statusCode = response.getStatusCode().value();
        List<BookDto> bookDtoList = response.getBody();

        assertEquals(statusCode,200);
        assertEquals(bookDtoList.size(),3);
    }

    @Test
    public void testGetWithInPriceRange_WhenPriceNotFound_ThenThrowNotFoundException() {
        when(bookRepo.findByPriceBetween(any(Double.class),any(Double.class), any(Pageable.class))).thenReturn(Page.empty());

        double value1 = 10.5;
        double value2 = 15.5;
        NotFoundError thrown = assertThrows(NotFoundError.class, () -> {bookService.getBookWithInPriceRange(value1,value2,
                this.pageSize, this.contentSize);});
        assertEquals(thrown.getMessage(), "Books with price starting from 10.5 and ending at 15.5, has not been found.");
    }



}