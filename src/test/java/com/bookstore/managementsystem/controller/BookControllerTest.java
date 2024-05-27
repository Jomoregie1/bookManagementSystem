package com.bookstore.managementsystem.controller;

import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


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
    @DisplayName("Test create book endpoint")
    public void testCreateBook_WhenNewBookCreated_ThenReturnSuccessStatus() throws Exception {

        // Arrange
        when(bookService.createBook(any(BookDto.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(bookDto));

        //Act & Assert
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(this.bookDto)))
                .andExpect(status().isCreated());


    }

    @Test
    @DisplayName("Test getting all books.")
    public void testGetAllBooks_WhenRequested_ThenReturnOkStatusAndNumberOfElements() throws Exception {
        // Arrange
        List<BookDto> bookList = List.of(this.bookDto, this.bookDto);
        when(bookService.getAllBooks()).thenReturn(ResponseEntity.status(HttpStatus.OK).body(bookList));

        // Act
        var response = mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Assert
        List<BookDto> bookResponse = mapper.readValue(response, new TypeReference<List<BookDto>>() {});
        assertEquals(bookResponse.size(), 2);


    }

    @Test
    @DisplayName("Test get book by Id.")
    public void testGetBookById_WhenIdEqualToOne_ThenReturnSuccessAndFoundBook() throws Exception {
        //Arrange
        when(bookService.getBook(any(Long.class))).thenReturn(ResponseEntity.status(HttpStatus.FOUND).body(this.bookDto));

        //Act & Assert
        var response = mockMvc.perform(get("/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andReturn().getResponse();

        // Assert
        String jsonResponse = response.getContentAsString();
        BookDto bookDto1 = mapper.readValue(jsonResponse, BookDto.class);

        assertEquals(bookDto.getTitle(), bookDto1.getTitle());
        assertEquals(bookDto.getIsbn(),bookDto1.getIsbn());
        assertEquals(bookDto.getPublicationDate(), bookDto1.getPublicationDate());

    }

    @Test
    @DisplayName("Test update book value")
    public void testUpdateBook_WhenNewBookObjectPassed_ThenReturnSuccess() throws Exception {

        // Arrange
        when(bookService.updateBook(any(Long.class),any(BookDto.class))).thenReturn(ResponseEntity.status(HttpStatus.OK).body(bookDto));

        // Act
        mockMvc.perform(put("/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk());

    }






}