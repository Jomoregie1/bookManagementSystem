package com.bookstore.managementsystem.Integration;

import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.entity.Author;
import com.bookstore.managementsystem.entity.Book;
import com.bookstore.managementsystem.repo.BookRepo;
import com.bookstore.managementsystem.utils.MapConvertor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class BookRepoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MapConvertor mapConvertor;

    @Autowired
    private TestEntityManager testEntityManager;

    private BookDto bookDto;
    private Book book;
    private ObjectMapper mapper;


    @BeforeEach
    void setUp() {

        Author author = Author.builder()
                .name("JK rowling")
                .id(1)
                .build();


        this.book = Book.builder()
                .id(1L)
                .price(10.5)
                .title("Harry and the lost animals")
                .author(author)
                .publicationDate(LocalDate.of(2010,10,5))
                .isbn(20202020L)
                .build();

        this.bookDto = mapConvertor.bookToBookDto(this.book);

        mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();


        testEntityManager.persistAndFlush(author);
        testEntityManager.persistAndFlush(this.book);
        testEntityManager.clear();

    }

    @Test
    public void testCreateBook_WhenBookDoesNotExist_ThenReturn201() throws Exception{

        // Arrange
        RequestBuilder postRequest = post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(this.bookDto));

        // Act
        var response = mockMvc.perform(postRequest).andReturn().getResponse();
        int statusCode = response.getStatus();
        BookDto bookDto1 = mapper.readValue(response.getContentAsString(), BookDto.class);

        //Assert
        assertEquals(201, statusCode);
        assertEquals(bookDto1.getIsbn(), this.bookDto.getIsbn());


    }


    @Test
    public void testGetAllBooksEndpoint_WhenBooksExist_ThenReturns200() throws Exception {
        // Arrange
        RequestBuilder postRequest = post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(this.bookDto));

        RequestBuilder getRequest = get("/books");

        //Act & Assert
        mockMvc.perform(postRequest).andExpect(status().isCreated());
        var getResponse = mockMvc.perform(getRequest).andExpect(status().isOk()).andReturn().getResponse();


        // Assert
        List<BookDto> retrievedBook = mapper.readValue(getResponse.getContentAsString(), new TypeReference<List<BookDto>>(){});
        assertEquals(retrievedBook.size(), 1);
        assertEquals(retrievedBook.get(0).getIsbn(), this.bookDto.getIsbn());
    }

//    @Test
//    public void testGetBook_WhenGivenBookIdExist_ThenReturn200() throws Exception {
//    }
}
