package com.vinsguru.user.exceptions;

public class UnknownTickerException extends RuntimeException {

    private static final String MESSAGE = "Ticker is not found";

    public UnknownTickerException() {
        super(MESSAGE);
    }

}
