package org.ethelred.temperature3;

import java.time.OffsetDateTime;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

public record TemperatureReading(OffsetDateTime recordedAt, double valueCelsius, String sourceName, String sourceType) {
    @JdbiConstructor
    public TemperatureReading {}

    public TemperatureReading(
            OffsetDateTime recordedAt, Temperature temperature, String sourceName, String sourceType) {
        this(recordedAt, temperature.celsius(), sourceName, sourceType);
    }

    public Temperature temperature() {
        return new Temperature(valueCelsius);
    }
}
