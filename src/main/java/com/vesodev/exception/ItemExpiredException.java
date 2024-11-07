package com.vesodev.exception;

import com.vesodev.model.Item;
import com.vesodev.util.DateUtils;

public class ItemExpiredException extends RuntimeException {

    private static final String MESSAGE = "Item %s has expired on %s";

    public ItemExpiredException(Item item) {
        super(MESSAGE.formatted(item.getName(), item.getExpirationDate().format(DateUtils.getDateFormater())));
    }
}
