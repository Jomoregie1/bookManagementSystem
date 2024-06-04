package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.AlreadyExistsError;
import com.bookstore.managementsystem.dto.AuthorDto;
import com.bookstore.managementsystem.entity.Author;
import com.bookstore.managementsystem.repo.AuthorRepo;
import com.bookstore.managementsystem.utils.MapConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {


    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private AuthorRepo authorRepo;

    @Mock
    private MapConvertor mapConvertor;

    private AuthorDto authorDto;
    private Author author;


    @BeforeEach
    void setUp() {

        this.authorDto = AuthorDto.builder()
                .name("John Doe")
                .build();

        this.author = Author.builder()
                .id(1L)
                .name("John Doe")
                .build();

    }

    @Test
    public void testCreateAuthor_WhenValidDtoObjectPassed_ThenReturnCreatedAuthorDto() throws AlreadyExistsError {
        // Arrange
        when(authorRepo.findAuthorByName(any(String.class))).thenReturn(Optional.empty());
        when(mapConvertor.authorDtoToAuthor(any(AuthorDto.class))).thenReturn(this.author);

        //Act
        ResponseEntity<AuthorDto> authorDtoResponseEntity = authorService.createAuthor(this.authorDto);
        int statusCode = authorDtoResponseEntity.getStatusCode().value();
        AuthorDto authorDto1 = authorDtoResponseEntity.getBody();

        //Assert
        assertEquals(statusCode, 201);
        assertEquals(authorDto1, this.authorDto);




    }







}