package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.entity.Book;
import com.bookstore.managementsystem.repo.BookRepo;
import com.bookstore.managementsystem.utils.MapConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

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
    public void testCreateBook_WhenNewBookObjectCreated_ReturnCreatedBookObjectAndOkStatus() {
        // Arrange
        Mockito.when(mapConvertor.BookDtoToBook(Mockito.any(BookDto.class)))
                .thenReturn(this.book);
        Mockito.when(bookRepo.save(Mockito.any(Book.class)))
                .thenReturn(book);

        //Act
        ResponseEntity<BookDto> response = bookService.createBook(bookDto);
        HttpStatusCode statusCode = response.getStatusCode();
        var responseBody = response.getBody();

        //Assert
        assertEquals(responseBody,bookDto);
        assertEquals(statusCode,HttpStatusCode.valueOf(200));

    }




}