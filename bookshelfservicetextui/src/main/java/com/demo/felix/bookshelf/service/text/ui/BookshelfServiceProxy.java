package com.demo.felix.bookshelf.service.text.ui;

import com.demo.felix.bookshelf.inventory.api.beans.Book;
import com.demo.felix.bookshelf.inventory.api.exceptions.BookAlreadyExistsException;
import com.demo.felix.bookshelf.inventory.api.exceptions.BookNotFoundException;
import com.demo.felix.bookshelf.inventory.api.exceptions.InvalidBookException;
import com.demo.felix.bookshelf.service.api.BookshelfService;
import com.demo.felix.bookshelf.service.api.exceptions.InvalidCredentialsException;
import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.HashSet;
import java.util.Set;

public class BookshelfServiceProxy {

    public static final String SCOPE = "book";
    public static final String[] FUNCTIONS = new String[]{"search", "add","remove","get"};
    private BundleContext context;

    public BookshelfServiceProxy(BundleContext context) {
        this.context = context;
    }


    @Descriptor("Search books by author, title, or category")
    public Set<Book> search(
            @Descriptor("username") String username,
            @Descriptor("password") String password,
            @Descriptor("search on attribute: author, title, or category") String attribute,
            @Descriptor("match like (use % at the beginning or end of <like> for wild-card)")
                    String filter) throws InvalidCredentialsException, BookNotFoundException {

        System.out.println("attribute:" + attribute + "-filter:" + filter);
        BookshelfService service = lookupService();
        String sessionId = service.login(username, password.toCharArray());
        Set<String> results;

        if ("title".equals(attribute)) {
            results = service.searchBooksByTitle(sessionId, filter);
        } else if ("author".equals(attribute)) {
            results = service.searchBooksByAuthor(sessionId, filter);
        } else if ("category".equals(attribute)) {
            results = service.searchBooksByCategory(sessionId, filter);
        } else {
            throw new RuntimeException("Invalid attribute, expecting one of { 'title', " +
                    "'author', 'category' } got '" + attribute + "'");
        }
        return getBooks(sessionId, service, results);
    }


    @Descriptor("Search books by rating")
    public Set<Book> search(
            @Descriptor("username") String username,
            @Descriptor("password") String password,
            @Descriptor("search on attribute: rating") String attribute,
            @Descriptor("lower rating limit (inclusive)") int lower,
            @Descriptor("upper rating limit (inclusive)") int upper)
            throws InvalidCredentialsException {
        System.out.println("attribute:" + attribute + "-lower:" + lower + "-upper:" + upper);

        if (!"rating".equals(attribute)) {
            throw new RuntimeException("Invalid attribute, expecting 'rating' got '" + attribute + "'");
        }
        BookshelfService service = lookupService();
        String sessionId = service.login(username, password.toCharArray());
        Set<String> results = service.searchBooksByRating(sessionId, lower, upper);
        return getBooks(sessionId, service, results);
    }

    @Descriptor("Add books")
    public String add(@Descriptor("username") String username,
                      @Descriptor("password") String password,
                      @Descriptor("ISBN") String isbn,
                      @Descriptor("Title") String title,
                      @Descriptor("Author") String author,
                      @Descriptor("Category") String category,
                      @Descriptor("Rating (0..10)") int rating)
            throws InvalidCredentialsException, BookAlreadyExistsException, InvalidBookException {
        BookshelfService service = lookupService();
        String sessionId = service.login(username, password.toCharArray());
        service.addBook(sessionId, isbn, title, author, category, rating);
        return isbn;
    }

    @Descriptor("Remove books by isbn")
    public String remove(@Descriptor("username") String username,
                      @Descriptor("password") String password,
                      @Descriptor("ISBN") String isbn)
            throws InvalidCredentialsException, BookNotFoundException {
        BookshelfService service = lookupService();
        String sessionId = service.login(username, password.toCharArray());
        service.removeBook(sessionId, isbn);
        return isbn;
    }

    @Descriptor("Get book by isbn")
    public Book get(@Descriptor("username") String username,
                         @Descriptor("password") String password,
                         @Descriptor("ISBN") String isbn)
            throws InvalidCredentialsException, BookNotFoundException {
        BookshelfService service = lookupService();
        String sessionId = service.login(username, password.toCharArray());
        return service.getBook(sessionId, isbn);
    }

    protected BookshelfService lookupService() {
        ServiceReference reference = context.getServiceReference(BookshelfService.class.getName());
        if (reference == null) {
            throw new RuntimeException("BookshelfService is not registered");
        }
        BookshelfService service = (BookshelfService) this.context.getService(reference);
        if (service == null) {
            throw new RuntimeException("BookshelfService is not registered");
        }
        return service;
    }

    private Set<Book> getBooks(String sessionId, BookshelfService service, Set<String> results) {
        Set<Book> books = new HashSet<>();
        results.forEach(isbn -> {
            try {
                Book book = service.getBook(sessionId, isbn);
                books.add(book);
            } catch (BookNotFoundException e) {
                System.err.println("ISBN " + isbn + " referenced but not found");
            }
        });
        return books;
    }

}
