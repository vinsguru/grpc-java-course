package com.vinsguru.user.exceptions;

public class InsufficientSharesException extends RuntimeException {

    private static final String MESSAGE = "User [id=%d] does not have enough shares to complete the transaction";

    public InsufficientSharesException(Integer userId) {
        super(MESSAGE.formatted(userId));
    }

}
