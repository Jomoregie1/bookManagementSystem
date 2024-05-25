package com.bookstore.managementsystem.controller;

import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.time.LocalDate;



@WebMvcTest(BookController.class)
class BookControllerTest {

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    private BookDto bookDto;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        this.bookDto = BookDto.builder()
                .isbn(12345678)
                .title("The best book ever")
                .publicationDate(LocalDate.of(2010, 10, 10))
                .build();

        mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

    }


    @Test
    public void testCreateBook_WhenNewBookCreated_ThenReturnSuccessStatus() throws Exception {

        // Arrange
        when(bookService.createBook(any(BookDto.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(bookDto));

        //Act & Assert
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(this.bookDto)))
                .andExpect(status().isCreated());


    }


}