package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.BookExistsError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.entity.Book;
import com.bookstore.managementsystem.repo.BookRepo;
import com.bookstore.managementsystem.utils.MapConvertor;
import org.aspectj.weaver.ast.Not;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private MapConvertor mapConvertor;

    @Mock
    private BookRepo bookRepo;
    private BookDto bookDto;
    private Book book;

    @BeforeEach
    void setUp() {

        this.bookDto = BookDto.builder()
                .price(10)
                .isbn(12345678)
                .title("Random title")
                .publicationDate(LocalDate.of(2020,1,10))
                .build();

        this.book = Book.builder()
                .price(10)
                .isbn(12345678)
                .title("Random title")
                .publicationDate(LocalDate.of(2020,1,10))
                .id(1)
                .build();

    }

    @Test
    @DisplayName("Test create book in service layer.")
    public void testCreateBook_WhenNewBookObjectCreated_ReturnCreatedBookObjectAndOkStatus() throws BookExistsError {
        // Arrange
        when(mapConvertor.BookDtoToBook(any(BookDto.class)))
                .thenReturn(this.book);
        when(bookRepo.save(any(Book.class)))
                .thenReturn(book);

        //Act
        ResponseEntity<BookDto> response = bookService.createBook(bookDto);
        HttpStatusCode statusCode = response.getStatusCode();
        var responseBody = response.getBody();

        //Assert
        assertEquals(responseBody,bookDto);
        assertEquals(statusCode,HttpStatusCode.valueOf(200));

    }


    @Test
    public void testCreateBook_WhenBookExistsErrorThrown_ThenTestExceptionThrow() throws BookExistsError {
        // Arrange
        when(bookRepo.existsByIsbn(any(Long.class)))
                .thenReturn(true);

        //Act & Assert
        BookExistsError thrown = assertThrows(BookExistsError.class, () -> {bookService.createBook(bookDto);});
        assertEquals("The book you are trying to create has been found in our system.", thrown.getMessage());


    }

    @Test
    public void testGetAllBooks_ThenReturnAListOfBookDtos() throws NotFoundError {
        //Arrange
        when(bookRepo.findAll()).thenReturn(List.of(book, book, book));
        when(mapConvertor.bookToBookDto(any(Book.class))).thenReturn(bookDto);
        // Act
        ResponseEntity<List<BookDto>> response = bookService.getAllBooks();
        //Assert
        int sizeOfResponse = response.getBody().size();
        assertEquals(sizeOfResponse, 3);

    }

    @Test
    public void testGetAllBooks_WhenFindAllReturnsEmptyList_ThenThrowNotFoundError() throws NotFoundError{
        when(bookRepo.findAll()).thenReturn(List.of());
        NotFoundError thrown = assertThrows(NotFoundError.class, () -> {
            bookService.getAllBooks();
        });
        assertEquals(thrown.getMessage(), "No Books Found");
    }

    @Test
    public void testGetBook_WhenBookIdPassed_ThenReturnBookFound() throws NotFoundError{
        when(bookRepo.findById(any(Long.class))).thenReturn(Optional.of(this.book));
        when(mapConvertor.bookToBookDto(any(Book.class))).thenReturn(this.bookDto);

        long testId = 1L;
        ResponseEntity<BookDto> response = bookService.getBook(testId);
        int status = response.getStatusCode().value();
        BookDto testBookDto = response.getBody();

        assertEquals(status,200);
        assertEquals(testBookDto, this.bookDto);

    }

    @Test
    public void testGetBook_WhenBookIdNotFound_ThenThrowNotFoundException() throws NotFoundError{
        when(bookRepo.findById(any(Long.class))).thenReturn(Optional.empty());

        long testId = 1L;
        NotFoundError thrown = assertThrows(NotFoundError.class, () -> {bookService.getBook(testId);});
        assertEquals(thrown.getMessage(), "The book with Id:1,Not Found.");
    }

    @Test
    public void testUpdateBook_WhenUpdatingBook_ThenReturnSucess() throws NotFoundError {
        when(bookRepo.existsById(any(Long.class)))
                .thenReturn(true);
        when(mapConvertor.BookDtoToBook(any(BookDto.class))).thenReturn(this.book);

        long bookId = 1L;

        ResponseEntity<BookDto> response = bookService.updateBook(bookId,this.bookDto);
        int statusCode = response.getStatusCode().value();
        BookDto testBookDto = response.getBody();

        assertEquals(statusCode, 200);
        assertEquals(testBookDto, bookDto);


    }

    @Test
    public void testUpdateBook_WhenUpdatingBookWithInvalidId_ThenAssertNotFoundErrorFound() throws NotFoundError{
        when(bookRepo.existsById(any(Long.class))).thenReturn(false);
        Long testId = 1L;

        NotFoundError thrown = assertThrows(NotFoundError.class, () -> {bookService.updateBook(testId, bookDto);});
        assertEquals(thrown.getMessage(),"Book with ID: 1 has not been found.");
    }
    


}