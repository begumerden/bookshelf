package com.demo.felix.bookshelf.inventory.impl.mock;

import com.demo.felix.bookshelf.inventory.api.beans.MutableBook;

/**
 * @author begum
 */
public class MutableBookImpl implements MutableBook {

    private String isbn;
    private String author;
    private String title;
    private String category;
    private int rating;

    protected MutableBookImpl(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
