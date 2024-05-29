package com.bookstore.managementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {

    private String message;
    private HttpStatusCode statusCode;
}
