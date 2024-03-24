package org.ethelred.temperature3;

import static org.assertj.core.api.Assertions.assertThat;

import io.avaje.inject.test.InjectTest;
import jakarta.inject.Inject;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;

@InjectTest
public class SaveReadingTest {
    @Inject
    ReadingsDao dao;

    @Test
    public void saveReading() {
        OffsetDateTime recordedAt = OffsetDateTime.of(2024, 3, 11, 17, 59, 0, 0, ZoneOffset.ofHours(-4));
        dao.saveTemperatureReadings(List.of(new TemperatureReading(recordedAt, 24.0, "test source", "test")));

        var load = dao.readAllTemperatureReadings();

        assertThat(load)
                .hasSize(1)
                .first()
                .isNotNull()
                .extracting(TemperatureReading::valueCelsius)
                .isEqualTo(24.0);

        dao.saveTemperatureReadings(List.of(new TemperatureReading(recordedAt, 23, "test source", "test")));

        load = dao.readAllTemperatureReadings();
        assertThat(load)
                .hasSize(1)
                .first()
                .isNotNull()
                .extracting(TemperatureReading::valueCelsius)
                .isEqualTo(24.0);
    }
}
