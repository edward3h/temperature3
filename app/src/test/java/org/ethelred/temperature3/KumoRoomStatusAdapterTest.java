package org.ethelred.temperature3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.avaje.jsonb.Jsonb;
import org.junit.jupiter.api.Test;

public class KumoRoomStatusAdapterTest {
    String sample =
            """
            {
              "r": {
                "indoorUnit": {
                  "status": {
                    "roomTemp": 20.5,
                    "mode": "heat",
                    "spCool": 21.5,
                    "spHeat": 20,
                    "vaneDir": "auto",
                    "fanSpeed": "auto",
                    "tempSource": "unset",
                    "activeThermistor": "unset",
                    "filterDirty": false,
                    "hotAdjust": false,
                    "defrost": false,
                    "standby": false,
                    "runTest": 0,
                    "humidTest": 0
                  }
                }
              }
            }
            """;

    @Test
    public void simpleTest() {
        var jsonbType = Jsonb.builder().build().type(RoomStatus.class);
        var result = jsonbType.fromJson(sample);
        assertEquals(20.5, result.roomTemp());
    }
}
