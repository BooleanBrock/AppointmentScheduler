package helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Helper class to convert time to UTC and back to local time
 *
 * @author Wyatt Brock
 */
public class TimeConverter {

    /**
     * Mehod to convert local time to UTC
     *
     * @param dateTime LocalDateTime local time
     * @return LocalDateTime in UTC
     */
    public static LocalDateTime convertToUTC(LocalDateTime dateTime) {

        ZonedDateTime dateTimeInMyZone = ZonedDateTime.of(dateTime, ZoneId.systemDefault());

        return dateTimeInMyZone.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

    }

    /**
     * Method to convert UTC to local time
     *
     * @param utcDateTime LocalDateTime time in UTC
     * @return LocalDateTime time in local time
     */
    public static LocalDateTime convertFromUTC(LocalDateTime utcDateTime){

        return ZonedDateTime.of(utcDateTime, ZoneId.of("UTC")).toOffsetDateTime().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }



}
