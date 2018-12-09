package com.demo.felix.bookshelf.inventory.api.beans;

/**
 * Book bean
 * @author begum
 */
public interface Book {

    String getIsbn();
    String getTitle();
    String getAuthor();
    String getCategory();
    int getRating();
}
