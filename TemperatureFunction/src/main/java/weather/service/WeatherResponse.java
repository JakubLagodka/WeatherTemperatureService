package weather.service;

public record WeatherResponse(
        String city,
        double temperatureCelsius,
        TemperatureCategory category,
        String observedAt,
        String source
) {
}
