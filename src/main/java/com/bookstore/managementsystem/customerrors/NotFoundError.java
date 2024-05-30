package com.bookstore.managementsystem.customerrors;

public class NotFoundError extends Exception{

    public NotFoundError(String message) {
        super(message);
    }
}
