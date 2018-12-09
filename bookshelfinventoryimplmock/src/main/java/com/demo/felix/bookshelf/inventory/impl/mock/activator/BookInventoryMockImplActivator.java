package com.demo.felix.bookshelf.inventory.impl.mock.activator;

import com.demo.felix.bookshelf.inventory.api.BookInventory;
import com.demo.felix.bookshelf.inventory.impl.mock.BookInventoryMockImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * instantiate and register the service on start and then unregister it on stop
 * @author begum
 */
public class BookInventoryMockImplActivator implements BundleActivator {

    private ServiceRegistration reg = null;

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("Starting Book Inventory Mock Impl");

        this.reg = context.registerService(BookInventory.class.getName(),
                new BookInventoryMockImpl(), null);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Stopping Book Inventory Mock Impl");

        if (this.reg != null) {
            context.ungetService(reg.getReference());
            this.reg = null;
        }
    }
}
