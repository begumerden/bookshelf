package com.demo.felix.bookshelf.service.impl.activator;

import com.demo.felix.bookshelf.service.api.BookshelfService;
import com.demo.felix.bookshelf.service.impl.BookshelfServiceImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

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
