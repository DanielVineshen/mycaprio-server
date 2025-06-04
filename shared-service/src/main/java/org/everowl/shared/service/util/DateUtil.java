package org.everowl.shared.service.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static String addDaysToToday(int days) {
        ZoneId malaysiaZone = ZoneId.of("Asia/Kuala_Lumpur");
        LocalDateTime futureDateTime = LocalDateTime.now(malaysiaZone).plusDays(days);

        return futureDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
