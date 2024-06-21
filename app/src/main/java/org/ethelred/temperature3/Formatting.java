package org.ethelred.temperature3;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class Formatting {
    public String formatDateTime(OffsetDateTime dateTime) {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateTime);
    }

    public String formatTemperature(Temperature temperature, Temperature.Unit unit) {
        return "%.1f°%s".formatted(temperature.temperature(unit), unit.symbol());
    }
}
