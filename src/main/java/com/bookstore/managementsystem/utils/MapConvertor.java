package com.bookstore.managementsystem.utils;
import com.bookstore.managementsystem.dto.AuthorDto;
import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.entity.Author;
import com.bookstore.managementsystem.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MapConvertor {


    @Mapping(source = "author.name", target = "authorName")
    BookDto bookToBookDto(Book book);

    @Mappings({
            @Mapping(source = "authorName", target = "author.name"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "orders", ignore = true)
    })
    Book bookDtoToBook(BookDto bookDto);

    AuthorDto authorToAuthorDto(Author author);
    
    Author authorDtoToAuthor(AuthorDto authorDto);

}
