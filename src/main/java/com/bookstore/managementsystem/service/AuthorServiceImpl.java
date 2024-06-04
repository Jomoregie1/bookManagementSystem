package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.dto.AuthorDto;
import com.bookstore.managementsystem.repo.AuthorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("AuthorService")
public class AuthorServiceImpl implements AuthorService{

    private AuthorRepo authorRepo;

    @Autowired
    AuthorServiceImpl(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    @Override
    public ResponseEntity<AuthorDto> createAuthor(AuthorDto authorDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<AuthorDto>> getAuthors() {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateAuthor(Long id, AuthorDto authorDto) {
        return null;
    }

    @Override
    public ResponseEntity<AuthorDto> getAuthorById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteAuthorById(Long id) {
        return null;
    }
}
