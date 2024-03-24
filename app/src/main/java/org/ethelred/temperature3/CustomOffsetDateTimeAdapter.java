package org.ethelred.temperature3;

import io.avaje.jsonb.CustomAdapter;
import io.avaje.jsonb.JsonAdapter;
import io.avaje.jsonb.JsonReader;
import io.avaje.jsonb.JsonWriter;
import io.avaje.jsonb.Jsonb;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@CustomAdapter
public class CustomOffsetDateTimeAdapter implements JsonAdapter<OffsetDateTime> {
    private final Jsonb jsonb;
    private final DateTimeFormatter formatter;

    public CustomOffsetDateTimeAdapter(Jsonb jsonb) {
        this.jsonb = jsonb;
        this.formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .parseLenient()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .appendLiteral(' ')
                .append(DateTimeFormatter.ISO_LOCAL_TIME)
                .appendOffset("+HHmm", "")
                .toFormatter();
    }

    @Override
    public void toJson(JsonWriter jsonWriter, OffsetDateTime offsetDateTime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OffsetDateTime fromJson(JsonReader jsonReader) {
        var stringValue = jsonReader.readString();
        return OffsetDateTime.parse(stringValue, formatter);
    }
}
