package com.demo.felix.bookshelf.inventory.api.exceptions;

/**
 * @author begum
 */
public class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}
