package com.bookstore.managementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private String title;
    private long isbn;
    private LocalDate publicationDate;
    private double price;
    private String authorName;
}
