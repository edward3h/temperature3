package org.ethelred.temperature3;

import io.avaje.jsonb.Jsonb;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomOffsetDateTimeAdapterTest {
    @Test
    public void simpleTest() {
        var jsonType = Jsonb.builder().build().type(SensorsAPI.SensorReading.class);
        var thing = jsonType.fromJson(
                """
    {
        "time": "2024-03-10 16:10:15-0400",
        "type": "F007TH",
        "channel": "6",
        "rolling_code": 196,
        "name": "6 Cold Frame",
        "temperature": 582,
        "humidity": 61,
        "battery_ok": false,
        "t_hist": 112,
        "h_hist": 94
    }
                """);
        Assertions.assertEquals(-14400, thing.time().getOffset().getTotalSeconds());
        Assertions.assertEquals(582, thing.temperature());
    }
}
