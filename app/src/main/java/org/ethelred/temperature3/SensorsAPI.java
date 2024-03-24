package org.ethelred.temperature3;

import io.avaje.http.api.Client;
import io.avaje.http.api.Get;
import io.avaje.http.api.RequestTimeout;
import io.avaje.jsonb.Json;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Client
public interface SensorsAPI {
    @Get("/api/sensors")
    @RequestTimeout(value = 10, chronoUnit = ChronoUnit.SECONDS)
    List<SensorReading> get();

    @Json
    record SensorReading(OffsetDateTime time, String name, int temperature, int humidity, boolean batteryOk) {}
}
