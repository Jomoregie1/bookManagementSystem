package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.dto.AuthorDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthorService {

    ResponseEntity<AuthorDto> createAuthor(AuthorDto authorDto);

    ResponseEntity<List<AuthorDto>>getAuthors();

    ResponseEntity<Void>updateAuthor(Long id, AuthorDto authorDto);

    ResponseEntity<AuthorDto> getAuthorById(Long id);

    ResponseEntity<Void>deleteAuthorById(Long id);
}
