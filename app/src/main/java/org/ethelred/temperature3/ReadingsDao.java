package org.ethelred.temperature3;

import java.time.OffsetDateTime;
import java.util.List;
import org.jdbi.v3.sqlobject.GenerateSqlObject;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

@GenerateSqlObject
public interface ReadingsDao {
    @SqlBatch(
            """
            INSERT into temperature_reading(recorded_at, value_celsius, source_name, source_type)
            SELECT :recordedAt, :valueCelsius, :sourceName, :sourceType
            WHERE NOT EXISTS (SELECT id FROM temperature_reading WHERE recorded_at = :recordedAt AND source_name = :sourceName)
            """)
    void saveTemperatureReadings(@BindMethods Iterable<TemperatureReading> reading);

    @SqlQuery(
            """
                    SELECT recorded_at, value_celsius, source_name, source_type
                    FROM temperature_reading
                    """)
    @RegisterConstructorMapper(TemperatureReading.class)
    List<TemperatureReading> readAllTemperatureReadings();

    @SqlQuery(
            """
            SELECT recorded_at, value_celsius, source_name, source_type
            FROM temperature_reading
            WHERE recorded_at > :since
            ORDER BY recorded_at DESC
            """)
    @RegisterConstructorMapper(TemperatureReading.class)
    List<TemperatureReading> readTemperatureReadings(@Bind("since") OffsetDateTime since);
}
