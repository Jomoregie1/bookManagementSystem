package com.bookstore.managementsystem.customerrors;

public class AlreadyExistsError extends Exception{

    public AlreadyExistsError(String message) {
        super(message);
    }
}
