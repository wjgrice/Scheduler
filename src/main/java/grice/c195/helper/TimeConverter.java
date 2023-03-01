package grice.c195.helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeConverter {
    public static LocalDateTime localToUtc(LocalDateTime localDateTime) {
        ZonedDateTime localZoned = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utcZoned = localZoned.withZoneSameInstant(ZoneId.of("UTC"));
        return utcZoned.toLocalDateTime();
    }
    public static LocalDateTime utcToLocal(LocalDateTime utcDateTime) {
        ZonedDateTime utcZoned = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localZoned = utcZoned.withZoneSameInstant(ZoneId.systemDefault());
        return localZoned.toLocalDateTime();
    }
}
