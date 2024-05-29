package com.bookstore.managementsystem.customerrors;

public class BookExistsError extends Exception{

    public BookExistsError(String message) {
        super(message);
    }
}
