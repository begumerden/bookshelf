package com.demo.felix.bookshelf.service.impl;

import com.demo.felix.bookshelf.inventory.api.BookInventory;
import com.demo.felix.bookshelf.inventory.api.beans.Book;
import com.demo.felix.bookshelf.inventory.api.beans.MutableBook;
import com.demo.felix.bookshelf.inventory.api.exceptions.BookAlreadyExistsException;
import com.demo.felix.bookshelf.inventory.api.exceptions.BookNotFoundException;
import com.demo.felix.bookshelf.inventory.api.exceptions.InvalidBookException;
import com.demo.felix.bookshelf.service.api.BookshelfService;
import com.demo.felix.bookshelf.service.api.exceptions.BookInventoryNotRegisteredRuntimeException;
import com.demo.felix.bookshelf.service.api.exceptions.InvalidCredentialsException;
import com.demo.felix.bookshelf.service.api.exceptions.SessionNotValidRuntimeException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author begum
 */
public class BookshelfServiceImpl implements BookshelfService {

    private String session;
    private BundleContext context;

    public BookshelfServiceImpl(BundleContext context) {
        this.context = context;
    }

    public Set<String> getCategories(String session) {
        checkSession(session);
        return lookupBookInventory().getCategories();
    }

    public void addBook(String session, String isbn, String title, String author, String category, int rating) throws BookAlreadyExistsException, InvalidBookException {
        checkSession(session);

        BookInventory inv = lookupBookInventory();

        MutableBook book = inv.createBook(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setRating(rating);

        inv.storeBook(book);
    }

    public void modifyBookCategory(String session, String isbn, String category) throws BookNotFoundException, InvalidBookException {
        checkSession(session);

        BookInventory inv = lookupBookInventory();

        MutableBook book = inv.loadBookForEdit(isbn);
        book.setCategory(category);

        inv.storeBook(book);
    }

    public void modifyBookRating(String session, String isbn, int rating) throws BookNotFoundException, InvalidBookException {
        checkSession(session);

        BookInventory inv = lookupBookInventory();

        MutableBook book = inv.loadBookForEdit(isbn);
        book.setRating(rating);

        inv.storeBook(book);
    }

    public void removeBook(String session, String isbn) throws BookNotFoundException {
        checkSession(session);
        lookupBookInventory().removeBook(isbn);
    }

    public Book getBook(String session, String isbn) throws BookNotFoundException {
        checkSession(session);
        BookInventory inventory = lookupBookInventory();
        return inventory.loadBook(isbn);
    }

    public Set<String> searchBooksByCategory(String session, String categoryLike) {
        checkSession(session);
        Map<BookInventory.SearchCriteria,String> search = new HashMap<>();
        search.put(BookInventory.SearchCriteria.CATEGORY_LIKE, categoryLike);
        return lookupBookInventory().searchBooks(search);
    }

    public Set<String> searchBooksByAuthor(String session, String authorLike) {
        checkSession(session);
        Map<BookInventory.SearchCriteria,String> search = new HashMap<>();
        search.put(BookInventory.SearchCriteria.AUTHOR_LIKE, authorLike);
        return lookupBookInventory().searchBooks(search);
    }

    public Set<String> searchBooksByTitle(String session, String titleLike) {
        checkSession(session);
        Map<BookInventory.SearchCriteria,String> search = new HashMap<>();
        search.put(BookInventory.SearchCriteria.TITLE_LIKE, titleLike);
        return lookupBookInventory().searchBooks(search);
    }

    public Set<String> searchBooksByRating(String session, int ratingLower, int ratingUpper) {
        checkSession(session);
        BookInventory inv = lookupBookInventory();
        Map<BookInventory.SearchCriteria, String> search = new HashMap<>();
        search.put(BookInventory.SearchCriteria.RATING_LT, Integer.toString(ratingLower));
        search.put(BookInventory.SearchCriteria.RATING_GT, Integer.toString(ratingUpper));
        return inv.searchBooks(search);
    }

    public String login(String username, char[] password) throws InvalidCredentialsException {
        System.out.println("login: " + username);

        if ("admin".equals(username) && Arrays.equals(password, "admin".toCharArray())) {
            this.session = Long.toString(System.currentTimeMillis());
            return this.session;
        }
        throw new InvalidCredentialsException(username);
    }

    public void logout(String sessionId) {
        checkSession(sessionId);
        this.session = null;
    }

    public boolean isSessionValid(String sessionId) {
        return this.session != null && this.session.equals(sessionId);
    }

    protected void checkSession(String sessionId) {
        if (!isSessionValid(sessionId)) {
            try {
                throw new SessionNotValidRuntimeException(sessionId);
            } catch (SessionNotValidRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private BookInventory lookupBookInventory() {
        String name = BookInventory.class.getName();
        ServiceReference ref = this.context.getServiceReference(name);
        if (ref == null) {
            try {
                throw new BookInventoryNotRegisteredRuntimeException(name);
            } catch (BookInventoryNotRegisteredRuntimeException e) {
                e.printStackTrace();
            }
        }
        return (BookInventory) this.context.getService(ref);
    }
}
