package com.bookstore.managementsystem.controller;

import com.bookstore.managementsystem.customerrors.AlreadyExistsError;
import com.bookstore.managementsystem.dto.AuthorDto;
import com.bookstore.managementsystem.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@Qualifier("AuthorController")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping()
    public ResponseEntity<AuthorDto> createAuthor (@RequestBody AuthorDto authorDto) throws AlreadyExistsError {
        return this.authorService.createAuthor(authorDto);
    }

    @GetMapping()
    public ResponseEntity<List<AuthorDto>> getAuthors() {
        return this.authorService.getAuthors();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAuthor(@PathVariable("id") Long id, @RequestBody AuthorDto authorDto) {
        return this.authorService.updateAuthor(id, authorDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable("id") Long id) {
        return this.authorService.getAuthorById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable("id") Long id) {
        return this.authorService.deleteAuthorById(id);
    }


}
