package org.ethelred.temperature3;

import io.avaje.http.client.HttpClient;
import io.avaje.http.client.JsonbBodyAdapter;
import io.avaje.jsonb.Json;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Singleton
@Named("openweather")
public class OpenWeatherPoller implements Poller {
    private final HttpClient client;
    private final ReadingsDao dao;

    public OpenWeatherPoller(Configuration configuration, ReadingsDao dao) {
        this.client = HttpClient.builder()
                .baseUrl(configuration.getPollers().get("openweather").getUrl().toString())
                .bodyAdapter(new JsonbBodyAdapter())
                .build();
        this.dao = dao;
    }

    @Override
    public void poll() {
        var result = client.request().GET().bean(OpenWeatherResult.class);
        var current = result.current();
        var daily = result.daily().getFirst();
        var temperatureReadings = List.of(
                new TemperatureReading(
                        _dt(current.dt()),
                        new Temperature(current.temp(), Temperature.Unit.FAHRENHEIT),
                        "openweather_current",
                        "openweather"),
                new TemperatureReading(
                        _dt(daily.dt()),
                        new Temperature(daily.temp().max(), Temperature.Unit.FAHRENHEIT),
                        "openweather_daily_max",
                        "openweather"),
                new TemperatureReading(
                        _dt(daily.dt()),
                        new Temperature(daily.temp().min(), Temperature.Unit.FAHRENHEIT),
                        "openweather_daily_min",
                        "openweather"));
        dao.saveTemperatureReadings(temperatureReadings);
    }

    private OffsetDateTime _dt(long dt) {
        return Instant.ofEpochSecond(dt).atOffset(ZoneOffset.UTC);
    }

    @Json
    public record OpenWeatherResult(Current current, List<Daily> daily) {}

    public record Current(long dt, double temp, double humidity) {}

    public record Daily(long dt, DailyTemp temp, double humidity) {}

    public record DailyTemp(double min, double max) {}
}
