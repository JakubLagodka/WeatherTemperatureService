package weather.service;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TemperatureCategory {
    FREEZING("Freezing"),
    COLD("Cold"),
    MILD("Mild"),
    WARM("Warm"),
    HOT("Hot");

    private final String label;

    TemperatureCategory(String label) {
        this.label = label;
    }

    @JsonValue
    public String label() {
        return label;
    }
}
