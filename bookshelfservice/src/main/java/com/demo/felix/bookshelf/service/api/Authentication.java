package com.demo.felix.bookshelf.service.api;

import com.demo.felix.bookshelf.service.api.exceptions.InvalidCredentialsException;

/**
 * @author begum
 */
public interface Authentication {

    String login (String username, char[] password) throws InvalidCredentialsException;
    void logout(String sessionId);
    boolean isSessionValid(String sessionId);
 // TODO: make another bundle
}
