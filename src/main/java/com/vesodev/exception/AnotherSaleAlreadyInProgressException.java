package com.vesodev.exception;

public class AnotherSaleAlreadyInProgressException extends RuntimeException {
    private static final String MESSAGE = "Another sale already in progress";

    public AnotherSaleAlreadyInProgressException() {
        super(MESSAGE);
    }
}
