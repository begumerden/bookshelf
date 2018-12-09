package com.demo.felix.bookshelf.inventory.impl.mock;

import com.demo.felix.bookshelf.inventory.api.BookInventory;
import com.demo.felix.bookshelf.inventory.api.beans.Book;
import com.demo.felix.bookshelf.inventory.api.beans.MutableBook;
import com.demo.felix.bookshelf.inventory.api.exceptions.BookNotFoundException;
import com.demo.felix.bookshelf.inventory.api.exceptions.InvalidBookException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.demo.felix.bookshelf.inventory.impl.mock.helpers.SearchHelper.checkIntegerGreater;
import static com.demo.felix.bookshelf.inventory.impl.mock.helpers.SearchHelper.checkStringMatch;

/**
 * @author begum
 */
public class BookInventoryMockImpl implements BookInventory {

    public static final String DEFAULT_CATEGORY = "default";
    private Map<String, MutableBook> booksByISBN = new HashMap<String, MutableBook>();
    private Map<String, Integer> categories = new HashMap<>();

    @Override
    public Set<String> getCategories() {
        return this.categories.keySet();
    }

    @Override
    public MutableBook createBook(String isbn) {
        return new MutableBookImpl(isbn);
    }

    @Override
    public MutableBook loadBookForEdit(String isbn) throws BookNotFoundException {
        if (StringUtils.isBlank(isbn)) {
            throw new BookNotFoundException("ISBN is not set");
        }

        MutableBook book = this.booksByISBN.get(isbn);
        if (book == null) {
            throw new BookNotFoundException(isbn);
        }
        return book;
    }

    @Override
    public String storeBook(MutableBook book) throws InvalidBookException {
        if (book == null) {
            throw new InvalidBookException("Book obj is null");
        }

        String isbn = book.getIsbn();
        if (StringUtils.isBlank(isbn)) {
            throw new InvalidBookException("ISBN is not set");
        }

        this.booksByISBN.put(isbn, book);

        String category = book.getCategory();
        if (StringUtils.isBlank(category)) {
            category = DEFAULT_CATEGORY;
        }
        if (this.categories.containsKey(category)) {
            int count = this.categories.get(category);
            this.categories.put(category, count + 1);
        } else {
            this.categories.put(category, 1);
        }
        return isbn;
    }

    @Override
    public Book loadBook(String isbn) throws BookNotFoundException {
        return loadBookForEdit(isbn);
    }

    @Override
    public void removeBook(String isbn) throws BookNotFoundException {
        if (StringUtils.isNotBlank(isbn)) {
            Book book = this.booksByISBN.remove(isbn);
            if (book == null) {
                throw new BookNotFoundException(isbn);
            }

            String category = book.getCategory();
            if (StringUtils.isNotBlank(category)) {
                int count = this.categories.get(category);
                if (count == 1) {
                    this.categories.remove(category);
                } else {
                    this.categories.put(category, count - 1);
                }
            }
        }
    }

    @Override
    public Set<String> searchBooks(Map<SearchCriteria, String> criteria) {
        List<Book> books = new ArrayList<>();
        Collection<MutableBook> mutableBooks = this.booksByISBN.values();

        criteria.entrySet().forEach(entry -> mutableBooks.forEach(book -> {
            switch (entry.getKey()) {
                case AUTHOR_LIKE:
                    if (checkStringMatch(book.getAuthor(), entry.getValue())) {
                        books.add(book);
                    }
                    break;
                case ISBN_LIKE:
                    if (checkStringMatch(book.getIsbn(), entry.getValue())) {
                        books.add(book);
                    }
                    break;
                case CATEGORY_LIKE:
                    if (checkStringMatch(book.getCategory(), entry.getValue())) {
                        books.add(book);
                    }
                    break;
                case TITLE_LIKE:
                    if (checkStringMatch(book.getTitle(), entry.getValue())) {
                        books.add(book);
                    }
                    break;
                case RATING_GT:
                    if (checkIntegerGreater(book.getRating(), entry.getValue())) {
                        books.add(book);
                    }
                    break;
                case RATING_LT:
                    if (checkIntegerGreater(book.getRating(), entry.getValue())) {
                        books.add(book);
                    }
                    break;
            }
        }));

        return books.stream().map(item -> item.getIsbn()).collect(Collectors.toSet());
    }

    @Override
    public int getBookSize() {
        return this.booksByISBN.size();
    }

}
