package xyz.hellothomas.jedi.client.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final String PATTERN1 = "yyyy-MM-dd HH:mm:ss";

    private DateTimeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getDateTimePattern1() {
        return localDateTimeToPattern1(LocalDateTime.now());
    }

    public static String localDateTimeToPattern1(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(PATTERN1));
    }

}
