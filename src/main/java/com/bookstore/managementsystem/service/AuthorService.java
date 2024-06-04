package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.AlreadyExistsError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
import com.bookstore.managementsystem.dto.AuthorDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthorService {

    ResponseEntity<AuthorDto> createAuthor(AuthorDto authorDto) throws AlreadyExistsError;

    ResponseEntity<List<AuthorDto>>getAuthors() throws NotFoundError;

    ResponseEntity<Void>updateAuthor(Long id, AuthorDto authorDto);

    ResponseEntity<AuthorDto> getAuthorById(Long id);

    ResponseEntity<Void>deleteAuthorById(Long id);
}
