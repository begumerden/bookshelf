package com.demo.felix.bookshelf.inventory.api;

import com.demo.felix.bookshelf.inventory.api.beans.Book;
import com.demo.felix.bookshelf.inventory.api.beans.MutableBook;
import com.demo.felix.bookshelf.inventory.api.exceptions.BookAlreadyExistsException;
import com.demo.felix.bookshelf.inventory.api.exceptions.BookNotFoundException;
import com.demo.felix.bookshelf.inventory.api.exceptions.InvalidBookException;

import java.util.Map;
import java.util.Set;

/**
 * @author begum
 */
public interface BookInventory {
    enum SearchCriteria {
        ISBN_LIKE,
        TITLE_LIKE,
        AUTHOR_LIKE,
        CATEGORY_LIKE,
        RATING_GT,
        RATING_LT
    }

    Set<String> getCategories();

    MutableBook createBook(String isbn) throws BookAlreadyExistsException;

    MutableBook loadBookForEdit(String isbn) throws BookNotFoundException;

    String storeBook(MutableBook book) throws InvalidBookException;

    Book loadBook(String isbn) throws BookNotFoundException;

    void removeBook(String isbn) throws BookNotFoundException;

    Set<String> searchBooks(Map<SearchCriteria, String> criteria);

    int getBookSize();

}
