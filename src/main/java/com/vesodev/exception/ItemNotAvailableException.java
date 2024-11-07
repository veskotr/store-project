package com.vesodev.exception;

public class ItemNotAvailableException extends RuntimeException {

    private static final String MESSAGE = "Item %s not available";

    public ItemNotAvailableException(String itemName) {
        super(MESSAGE.formatted(itemName));
    }
}
