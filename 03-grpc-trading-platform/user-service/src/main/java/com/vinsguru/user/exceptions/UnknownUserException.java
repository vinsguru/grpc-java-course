package com.vinsguru.user.exceptions;

public class UnknownUserException extends RuntimeException {

    private static final String MESSAGE = "User [id=%d] is not found";

    public UnknownUserException(Integer userId) {
        super(MESSAGE.formatted(userId));
    }

}
