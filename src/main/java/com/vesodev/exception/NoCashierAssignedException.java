package com.vesodev.exception;

public class NoCashierAssignedException extends RuntimeException {
    private static final String MESSAGE = "No cashier assigned";

    public NoCashierAssignedException() {
        super(MESSAGE);
    }
}
