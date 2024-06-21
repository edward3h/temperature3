package org.ethelred.temperature3;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
@Named("sensors")
public class SensorsPoller implements Poller {
    private final SensorsAPI api;
    private final ReadingsDao dao;

    public SensorsPoller(SensorsAPI api, ReadingsDao dao) {
        this.api = api;
        this.dao = dao;
    }

    @Override
    public void poll() {
        var response = api.get();
        var temperatureReadings = response.stream()
                .filter(sensorReading -> sensorReading.name() != null)
                .map(this::asTemperatureReading)
                .toList();
        dao.saveTemperatureReadings(temperatureReadings);
    }

    private TemperatureReading asTemperatureReading(SensorsAPI.SensorReading sensorReading) {
        return new TemperatureReading(
                sensorReading.time(),
                Temperature.fromScaledInt(sensorReading.temperature(), 1, Temperature.Unit.FAHRENHEIT)
                        .celsius(),
                sensorReading.name(),
                "sensor");
    }
}
