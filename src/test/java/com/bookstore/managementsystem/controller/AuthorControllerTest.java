package com.bookstore.managementsystem.controller;

import com.bookstore.managementsystem.dto.AuthorDto;
import com.bookstore.managementsystem.entity.Book;
import com.bookstore.managementsystem.repo.AuthorRepo;
import com.bookstore.managementsystem.repo.BookRepo;
import com.bookstore.managementsystem.repo.OrderRepo;
import com.bookstore.managementsystem.service.AuthorService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(value = AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private AuthorRepo authorRepo;

    @MockBean
    private OrderRepo orderRepo;

    @MockBean
    private BookRepo bookRepo;

    private AuthorDto authorDto;
    private ObjectMapper mapper;


    @BeforeEach
    void setup() {
        this.authorDto = AuthorDto.builder()
                .name("Author1")
                .build();


        this.mapper = new ObjectMapper();

    }

    @Test
    public void testCreateNewAuthor_returnCreatedStatusAndAuthorObject() throws Exception {
//        Arrange
            when(authorService.createAuthor(any(AuthorDto.class)))
                    .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(this.authorDto));


        // Act & Assert
        var response = mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(this.authorDto)))
                .andExpect(status().isCreated())
                .andReturn();

        // Assert
//        AuthorDto authorDto1 = mapper.readValue(response.getResponse().getContentAsString(), AuthorDto.class);
//        assertEquals(authorDto1.getName(), this.authorDto.getName());


    }

    @Test
    public void testGetAllAuthors_ReturnSuccessAndAListOfAuthors() throws Exception {

        // Arrange
        when(authorService.getAuthors())
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(List.of(this.authorDto, this.authorDto)));

        var response = mockMvc.perform(get("/authors"))
                .andReturn();

        // Assert status code is 200.
        int status = response.getResponse().getStatus();
        assertEquals(status,200);

        //Assert number of items returned
        List<AuthorDto> authors = mapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<AuthorDto>>() {});
        assertEquals(authors.size(), 2);

    }


    @Test
    public void testUpdateAuthor_ReturnSuccess() throws Exception {
        // Arrange
        when(authorService.updateAuthor(any(Long.class), any(AuthorDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        // Act
        var response = mockMvc.perform(put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(this.authorDto)))
                        .andReturn();

        // Assert
        int status = response.getResponse().getStatus();
        assertEquals(status, 200);
    }


    @Test
    public void testGetAuthorById_ReturnAuthorAndSuccessStatusCode() throws Exception {
        // Arrange
        when(authorService.getAuthorById(any(Long.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(this.authorDto));

        // Act
        var response = mockMvc.perform(get("/authors/1")).andReturn();

        // Assert
        int status = response.getResponse().getStatus();
        assertEquals(status,200);
        AuthorDto authorDto1 = mapper.readValue(response.getResponse().getContentAsString(),AuthorDto.class);
        assertEquals(authorDto1.getName(), this.authorDto.getName());
    }

    @Test
    public void testDeleteAuthorById_ReturnSuceess() throws Exception{
//     Arrange
        when(authorService.deleteAuthorById(any(Long.class))).thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        //Act
        var response = mockMvc.perform(delete("/authors/1")).andReturn();
        int status = response.getResponse().getStatus();

        // Assert
        assertEquals(status, 200);
    }

}