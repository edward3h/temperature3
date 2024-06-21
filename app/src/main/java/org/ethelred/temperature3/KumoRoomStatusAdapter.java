package org.ethelred.temperature3;

import io.avaje.jsonb.CustomAdapter;
import io.avaje.jsonb.JsonAdapter;
import io.avaje.jsonb.JsonDataException;
import io.avaje.jsonb.JsonReader;
import io.avaje.jsonb.JsonWriter;
import io.avaje.jsonb.Jsonb;

@CustomAdapter
public class KumoRoomStatusAdapter implements JsonAdapter<RoomStatus> {
    public KumoRoomStatusAdapter(Jsonb jsonb) {}

    @Override
    public void toJson(JsonWriter writer, RoomStatus value) {
        throw new UnsupportedOperationException("toJson");
    }

    /* example
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
         */

    @Override
    public RoomStatus fromJson(JsonReader reader) {
        RoomStatus r = null;
        double roomTemp = 0.0;
        String mode = "";
        double spCool = 0.0;
        double spHeat = 0.0;
        boolean valid = true;

        reader.beginObject();
        if (reader.hasNextField() && "r".equals(reader.nextField())) {
            reader.beginObject();
            if (reader.hasNextField() && "indoorUnit".equals(reader.nextField())) {
                reader.beginObject();
                if (reader.hasNextField() && "status".equals(reader.nextField())) {
                    reader.beginObject();
                    while (reader.hasNextField()) {
                        var field = reader.nextField();
                        try {
                            switch (field) {
                                case "roomTemp" -> roomTemp = reader.readDouble();
                                case "mode" -> mode = reader.readString();
                                case "spCool" -> spCool = reader.readDouble();
                                case "spHeat" -> spHeat = reader.readDouble();
                                default -> reader.skipValue();
                            }
                        } catch (JsonDataException e) {
                            valid = false;
                        }
                    }
                    if (valid) {
                        r = new RoomStatus(roomTemp, mode, spCool, spHeat);
                    }
                    reader.endObject();
                }
                reader.endObject();
            }
            reader.endObject();
        }
        reader.endObject();
        return r;
    }
}
