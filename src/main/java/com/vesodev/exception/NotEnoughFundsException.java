package com.vesodev.exception;

public class NotEnoughFundsException extends RuntimeException {
    private static final String MESSAGE = "Not enough funds";

    public NotEnoughFundsException() {
        super(MESSAGE);
    }
}
