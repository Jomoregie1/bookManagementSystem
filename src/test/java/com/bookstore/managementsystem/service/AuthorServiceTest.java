package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.AlreadyExistsError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
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

import java.util.List;
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

    @Test
    public void testCreateAuthor_WhenGivenAuthorDtoIsPresent_ThenThrowAlreadyExistsError() {
        when(authorRepo.findAuthorByName(any(String.class))).thenReturn(Optional.of(this.author));

        AlreadyExistsError raisedError = assertThrows(AlreadyExistsError.class, () -> {authorService.createAuthor(this.authorDto);});

        assertEquals(raisedError.getMessage(), "An Author with the name: John Doe already exists.");
    }

    @Test
    public void testGetAuthors_WhenCalled_ThenReturnAListOfAllAuthors() throws NotFoundError {
        when(authorRepo.findAll()).thenReturn(List.of(this.author,this.author,this.author));
        when(mapConvertor.authorToAuthorDto(any(Author.class))).thenReturn(this.authorDto);

        ResponseEntity<List<AuthorDto>> responseEntityAuthorDtoList = authorService.getAuthors();
        int statusCode = responseEntityAuthorDtoList.getStatusCode().value();
        List<AuthorDto> authorDtoList = responseEntityAuthorDtoList.getBody();

        assertEquals(200, statusCode);
        assertEquals(3, authorDtoList.size());

    }

    @Test
    public void testGetAuthors_whenListOfAuthorsIsEmpty_ThenThrowNotFoundError() throws NotFoundError{
        when(authorRepo.findAll()).thenReturn(List.of());

        NotFoundError raisedError = assertThrows(NotFoundError.class, () -> {authorService.getAuthors();});

        assertEquals(raisedError.getMessage(), "No Authors have been found.");


    }

    @Test
    public void testUpdateAuthor_WhenValidIdProvided_ThenReturn200() throws NotFoundError{
        when(authorRepo.findById(any(Long.class))).thenReturn(Optional.of(this.author));
        long testId = 1L;

        ResponseEntity<Void> responseEntity = authorService.updateAuthor(testId, this.authorDto);
        assertEquals(200, responseEntity.getStatusCode().value());

    }

    @Test
    public void testUpdateAuthor_WhenInvalidIdProvided_ThenThrowNotFoundError() throws NotFoundError{
        when(authorRepo.findById(any(Long.class))).thenReturn(Optional.empty());
        long testId = 1L;

        NotFoundError raisedError = assertThrows(NotFoundError.class, () -> {authorService.updateAuthor(testId,authorDto);});
        assertEquals("No Author Found with the given Id: 1.", raisedError.getMessage());
    }

    @Test
    public void testGetAuthorById_WhenAuthorPresent_ThenReturnAuthor() {
        when(authorRepo.findById(any(Long.class))).thenReturn(Optional.of(this.author));
        when(mapConvertor.authorToAuthorDto(any(Author.class))).thenReturn(this.authorDto);
        long testId = 1L;

        ResponseEntity<AuthorDto> authorDtoResponseEntity = authorService.getAuthorById(testId);
        int statusCode = authorDtoResponseEntity.getStatusCode().value();
        AuthorDto authorDto1 = authorDtoResponseEntity.getBody();

        assertEquals(200, statusCode);
        assertEquals(this.authorDto, authorDto1);
    }

    






}