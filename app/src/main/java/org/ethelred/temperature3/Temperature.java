package org.ethelred.temperature3;

public record Temperature(double celsius) {
    public enum Unit {
        CELSIUS,
        FAHRENHEIT {
            @Override
            double toCelsius(double v) {
                return (v - 32.0) * 5.0 / 9.0;
            }
        };

        double toCelsius(double v) {
            return v;
        }
    }

    public Temperature(double value, Unit unit) {
        this(unit.toCelsius(value));
    }

    public static Temperature fromScaledInt(int value, int scale, Unit unit) {
        return new Temperature(unit.toCelsius(((double) value) / Math.pow(10, scale)));
    }

    public double fahrenheit() {
        return celsius * 9.0 / 5.0 + 32.0;
    }
}
