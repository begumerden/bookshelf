import com.demo.felix.bookshelf.inventory.impl.mock.BookInventoryMockImpl;
import com.demo.felix.bookshelf.inventory.api.BookInventory;
import com.demo.felix.bookshelf.inventory.api.beans.Book;
import com.demo.felix.bookshelf.inventory.api.beans.MutableBook;
import com.demo.felix.bookshelf.inventory.api.exceptions.BookNotFoundException;
import com.demo.felix.bookshelf.inventory.api.exceptions.InvalidBookException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author begum
 */
public class InventoryTest {

    private BookInventoryMockImpl bookInventoryMock;

    @Before
    public void init() {
        bookInventoryMock = new BookInventoryMockImpl();
    }

    @Test
    public void successful_create_book() throws InvalidBookException {
        MutableBook book1 = bookInventoryMock.createBook("1");
        book1.setTitle("Title 1");
        book1.setAuthor("Author 1");
        book1.setCategory("CAT1");
        book1.setRating(1);

        String isbn = bookInventoryMock.storeBook(book1);
        assertNotNull(book1);
        assertTrue(isbn.equals("1"));
    }


    @Test
    public void successful_remove_book() throws BookNotFoundException, InvalidBookException {
        MutableBook book1 = bookInventoryMock.createBook("1");
        book1.setTitle("Title 1");
        book1.setAuthor("Author 1");
        book1.setCategory("CAT1");
        book1.setRating(1);

        bookInventoryMock.storeBook(book1);
        assertTrue(bookInventoryMock.getBookSize() == 1);

        bookInventoryMock.removeBook(book1.getIsbn());
        assertTrue(bookInventoryMock.getBookSize() == 0);
    }

    @Test
    public void successful_load_book() throws BookNotFoundException, InvalidBookException {
        MutableBook book1 = bookInventoryMock.createBook("1");
        book1.setTitle("Title 1");
        book1.setAuthor("Author 1");
        book1.setCategory("CAT1");
        book1.setRating(1);
        bookInventoryMock.storeBook(book1);

        Book book = bookInventoryMock.loadBook("1");
        assertNotNull(book);
        assertTrue(book.getAuthor().equals("Author 1"));
    }

    @Test
    public void successful_search_book() throws InvalidBookException, BookNotFoundException {
        MutableBook book1 = bookInventoryMock.createBook("1");
        assertNotNull(book1);
        book1.setTitle("Title 1");
        book1.setAuthor("Author 1");
        book1.setCategory("CAT1");
        book1.setRating(1);
        bookInventoryMock.storeBook(book1);

        MutableBook book2 = bookInventoryMock.createBook("2");
        book2.setTitle("Title 2");
        book2.setAuthor("Author 2");
        book2.setCategory("CAT2");
        bookInventoryMock.storeBook(book2);

        Map<BookInventory.SearchCriteria, String> searchMap = new HashMap<>();
        searchMap.put(BookInventory.SearchCriteria.AUTHOR_LIKE, "%1");
        searchMap.put(BookInventory.SearchCriteria.TITLE_LIKE, "%2");
        Set<String> foundIsbns = bookInventoryMock.searchBooks(searchMap);
        System.out.println("found isbns:" + foundIsbns);
        assertNotNull(foundIsbns);
        assertTrue(foundIsbns.size() > 1);
    }


    @Test(expected = BookNotFoundException.class)
    public void load_book_should_fail_when_non_exist_isbn_book() throws BookNotFoundException {
        bookInventoryMock.loadBook("12");
    }

    @Test(expected = BookNotFoundException.class)
    public void remove_book_should_fail_when_invalid_isbn() throws BookNotFoundException {
        bookInventoryMock.loadBook("1111");
    }
}
