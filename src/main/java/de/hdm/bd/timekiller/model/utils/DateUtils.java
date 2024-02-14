package de.hdm.bd.timekiller.model.utils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtils {
    public static Date startAsDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    public static Date endAsDate(LocalDate localDate) {
        return Date.from(localDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC));
    }
}
