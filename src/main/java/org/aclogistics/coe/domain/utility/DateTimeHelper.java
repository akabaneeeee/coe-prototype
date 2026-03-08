package org.aclogistics.coe.domain.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.experimental.UtilityClass;

/**
 * @author Rosendo Coquilla
 */
@UtilityClass
public class DateTimeHelper {

    private static final String ZONE_ID_MANILA = "Asia/Manila";
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String FULL_DATE_TIME_FORMAT = "MMMM d, yyyy h:mm a";
    private static final String FULL_DATE_FORMAT = "MMMM d, uuuu";
    private static final String FILENAME_DATE_TIME_FORMAT = "yyyyMMdd_HHmmssSSS";
    private static final String MONTH_COE_FORMAT = "MMMM";

    public static final ZoneId ZONE_MANILA = ZoneId.of(ZONE_ID_MANILA);
    public static final ZoneId ZONE_UTC = ZoneId.of("UTC");

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
    private static final DateTimeFormatter FULL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(FULL_DATE_TIME_FORMAT, Locale.ENGLISH);
    private static final DateTimeFormatter FULL_DATE_FORMATTER = DateTimeFormatter.ofPattern(FULL_DATE_FORMAT);
    private static final DateTimeFormatter FILENAME_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(FILENAME_DATE_TIME_FORMAT);
    private static final DateTimeFormatter MONTH_COE_FORMATTER = DateTimeFormatter.ofPattern(MONTH_COE_FORMAT);

    public LocalDateTime getTodayInPHTDefaultFormat() {
        return LocalDateTime.parse(LocalDateTime.now(ZONE_MANILA).format(formatter), formatter);
    }

    public String getTodayInPHTDefaultStringFormat() {
        return LocalDateTime.now(ZONE_MANILA).format(formatter);
    }

    public LocalDateTime convertToPHTDefaultFormat(LocalDateTime dateTime) {
        return LocalDateTime.parse(
            dateTime.atZone(ZONE_UTC)
                .withZoneSameInstant(ZONE_MANILA)
                .toLocalDateTime()
                .format(formatter),
            formatter
        );
    }

    public LocalDateTime convertToUTCDefaultFormat(LocalDateTime dateTime) {
        return LocalDateTime.parse(
            dateTime.atZone(ZONE_MANILA)
                .withZoneSameInstant(ZONE_UTC
                ).toLocalDateTime()
                .format(formatter),
            formatter
        );
    }

    public String convertToFullDateTimeFormat(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(FULL_DATE_TIME_FORMATTER) : "";
    }

    public String convertToFullDateFormat(LocalDate date) {
        return date.format(FULL_DATE_FORMATTER);
    }

    public String getTodayPHTFileNameFormat() {
        return LocalDateTime.now(ZONE_MANILA).format(FILENAME_DATE_TIME_FORMATTER);
    }

    public String convertToMonthCOEFormat(LocalDate date) {
        return date.format(MONTH_COE_FORMATTER);
    }
}
