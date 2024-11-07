package com.vesodev.exception;

import com.vesodev.model.ItemType;

public class ItemNotSupportedException extends RuntimeException {
    private static final String MESSAGE = "Item type %s is not supported";

    public ItemNotSupportedException(ItemType itemType) {
        super(MESSAGE.formatted(itemType));
    }
}
