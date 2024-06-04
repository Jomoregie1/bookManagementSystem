package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.AlreadyExistsError;
import com.bookstore.managementsystem.dto.AuthorDto;
import com.bookstore.managementsystem.entity.Author;
import com.bookstore.managementsystem.repo.AuthorRepo;
import com.bookstore.managementsystem.utils.MapConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("AuthorService")
public class AuthorServiceImpl implements AuthorService{

    private AuthorRepo authorRepo;
    private MapConvertor mapConvertor;

    @Autowired
    AuthorServiceImpl(AuthorRepo authorRepo, MapConvertor mapConvertor) {
        this.authorRepo = authorRepo;
        this.mapConvertor = mapConvertor;
    }

    @Override
    public ResponseEntity<AuthorDto> createAuthor(AuthorDto authorDto) throws AlreadyExistsError {
        Optional<Author> authorOptional = authorRepo.findAuthorByName(authorDto.getName());

        if (authorOptional.isPresent()) {
            throw new AlreadyExistsError("An Author with the name: " + authorDto.getName() + " already exists.");
        }

        Author author = mapConvertor.authorDtoToAuthor(authorDto);
        authorRepo.save(author);

        return ResponseEntity.status(HttpStatus.CREATED).body(authorDto);
    }

    @Override
    public ResponseEntity<List<AuthorDto>> getAuthors() {
        List<Author> authors = authorRepo.findAll();
        List<AuthorDto> authorDtoList = authors.stream()
                .map(mapConvertor::authorToAuthorDto)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(authorDtoList);
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
