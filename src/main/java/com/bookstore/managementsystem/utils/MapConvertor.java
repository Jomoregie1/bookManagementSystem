package com.bookstore.managementsystem.utils;
import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapConvertor {

    BookDto bookToBookDto(Book book);
    Book BookDtoToBook(BookDto bookDto);

}
