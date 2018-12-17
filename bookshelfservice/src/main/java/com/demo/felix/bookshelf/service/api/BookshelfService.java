package com.demo.felix.bookshelf.service.api;

import com.demo.felix.bookshelf.inventory.api.beans.Book;
import com.demo.felix.bookshelf.inventory.api.exceptions.BookAlreadyExistsException;
import com.demo.felix.bookshelf.inventory.api.exceptions.BookNotFoundException;
import com.demo.felix.bookshelf.inventory.api.exceptions.InvalidBookException;

import java.util.Set;

/**
 * @author begum
 */
public interface BookshelfService extends Authentication {

    Set<String> getCategories(String sessionId);

    void addBook(String session, String isbn, String title, String author, String category, int rating)
            throws BookAlreadyExistsException, InvalidBookException;

    void modifyBookCategory(String session, String isbn, String category)
            throws BookNotFoundException, InvalidBookException;

    void modifyBookRating(String session, String isbn, int rating)
            throws BookNotFoundException, InvalidBookException;

    void removeBook(String session, String isbn) throws BookNotFoundException;

    Book getBook(String session, String isbn) throws BookNotFoundException;

    Set<String> searchBooksByCategory(String session, String categoryLike);

    Set<String> searchBooksByAuthor(String session, String authorLike);

    Set<String> searchBooksByTitle(String session, String titleLike);

    Set<String> searchBooksByRating(String session, int ratingLower, int ratingUpper);
}
