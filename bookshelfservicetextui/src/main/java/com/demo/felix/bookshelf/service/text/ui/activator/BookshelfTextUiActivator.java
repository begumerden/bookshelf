package com.demo.felix.bookshelf.service.text.ui.activator;

import com.demo.felix.bookshelf.service.text.ui.BookshelfServiceProxy;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Hashtable;
/**
 * @author begum
 */
public class BookshelfTextUiActivator implements BundleActivator {

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("BookshelfTextUiActivator started");
        Hashtable props = new Hashtable();
        props.put("osgi.command.scope", BookshelfServiceProxy.SCOPE);
        props.put("osgi.command.function", BookshelfServiceProxy.FUNCTIONS);

        bundleContext.registerService(
                BookshelfServiceProxy.class.getName(),
                new BookshelfServiceProxy(bundleContext),
                props);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("BookshelfTextUiActivator stopped");

    }
}
