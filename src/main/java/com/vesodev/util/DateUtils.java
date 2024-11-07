package com.vesodev.util;

import java.time.format.DateTimeFormatter;

public final class DateUtils {
    private DateUtils() {
    }

    private static DateTimeFormatter dateFormater;
    private static DateTimeFormatter dateTimeFormater;

    public static DateTimeFormatter getDateFormater() {
        if (dateFormater == null) {
            dateFormater = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        }
        return dateFormater;
    }

    public static DateTimeFormatter getDateTimeFormater() {
        if (dateTimeFormater == null) {
            dateTimeFormater = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        }
        return dateTimeFormater;
    }
}
