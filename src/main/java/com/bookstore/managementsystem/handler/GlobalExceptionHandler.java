package com.bookstore.managementsystem.handler;

import com.bookstore.managementsystem.customerrors.BookExistsError;
import com.bookstore.managementsystem.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookExistsError.class)
    public ResponseEntity<ErrorDto> bookExistsException(BookExistsError bookExistsError){
        ErrorDto error = ErrorDto.builder()
                .statusCode(HttpStatusCode.valueOf(404))
                .message(bookExistsError.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
