package com.bookstore.managementsystem.customerrors;

public class DatabaseAccessError extends Exception{

    public DatabaseAccessError(String message) {
        super(message);
    }
}
