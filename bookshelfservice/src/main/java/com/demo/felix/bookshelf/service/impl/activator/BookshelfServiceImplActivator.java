package com.demo.felix.bookshelf.service.impl.activator;

import com.demo.felix.bookshelf.inventory.api.BookInventory;
import com.demo.felix.bookshelf.inventory.api.beans.Book;
import com.demo.felix.bookshelf.inventory.api.exceptions.BookAlreadyExistsException;
import com.demo.felix.bookshelf.inventory.api.exceptions.BookNotFoundException;
import com.demo.felix.bookshelf.inventory.api.exceptions.InvalidBookException;
import com.demo.felix.bookshelf.service.api.BookshelfService;
import com.demo.felix.bookshelf.service.impl.BookshelfServiceImpl;
import com.demo.felix.bookshelf.service.impl.exceptions.InvalidCredentialsException;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import java.util.Set;

/**
 * @author begum
 */
public class BookshelfServiceImplActivator implements BundleActivator {
    ServiceRegistration reg = null;

    public void start(BundleContext context) throws Exception {
        System.out.println("BookshelfServiceImplActivator start . .");
        this.reg = context.registerService(BookshelfService.class.getName(),
                new BookshelfServiceImpl(context), null);

    }

    public void stop(BundleContext context) throws Exception {
        if (this.reg != null) {
            context.ungetService(reg.getReference());
        }
    }

}
