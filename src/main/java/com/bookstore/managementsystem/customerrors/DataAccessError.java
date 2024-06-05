package com.bookstore.managementsystem.customerrors;

public class DataAccessError extends Exception{

    public DataAccessError(String message) {
        super(message);
    }
}
