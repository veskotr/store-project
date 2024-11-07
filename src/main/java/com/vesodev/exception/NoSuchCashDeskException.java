package com.vesodev.exception;

public class NoSuchCashDeskException extends RuntimeException {
    private static final String MESSAGE = "No such cash desk exists";

    public NoSuchCashDeskException() {
        super(MESSAGE);
    }
}
