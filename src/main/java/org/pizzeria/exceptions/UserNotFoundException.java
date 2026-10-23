package org.pizzeria.exceptions;

import org.pizzeria.common.BaseException;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
