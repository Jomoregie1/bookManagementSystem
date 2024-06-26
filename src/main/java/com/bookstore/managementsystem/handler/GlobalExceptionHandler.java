package com.bookstore.managementsystem.handler;

import com.bookstore.managementsystem.customerrors.AlreadyExistsError;
import com.bookstore.managementsystem.customerrors.DatabaseAccessError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
import com.bookstore.managementsystem.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyExistsError.class)
    public ResponseEntity<ErrorDto> alreadyExistsException(AlreadyExistsError alreadyExistsError){
        ErrorDto error = ErrorDto.builder()
                .statusCode(HttpStatusCode.valueOf(404))
                .message(alreadyExistsError.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(NotFoundError.class)
    public ResponseEntity<ErrorDto> notFoundError(NotFoundError notFoundError) {
        ErrorDto errorDto = ErrorDto.builder()
                .message(notFoundError.getMessage())
                .statusCode(HttpStatus.valueOf(40))
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(DatabaseAccessError.class)
    public ResponseEntity<ErrorDto> databaseAccessError (DatabaseAccessError databaseAccessError) {
        ErrorDto errorDto = ErrorDto.builder()
                .statusCode(HttpStatus.GATEWAY_TIMEOUT)
                .message(databaseAccessError.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(errorDto);
    }
}
