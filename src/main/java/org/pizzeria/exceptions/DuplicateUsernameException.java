package org.pizzeria.exceptions;

import org.pizzeria.common.BaseException;

public class DuplicateUsernameException extends BaseException {


    public DuplicateUsernameException(String message) {
        super(message);
    }
}
