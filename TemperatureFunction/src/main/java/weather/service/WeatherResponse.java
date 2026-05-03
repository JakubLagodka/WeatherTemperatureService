package weather.service;

public record WeatherResponse(
        String city,
        String temperatureCelsius,
        String category,
        String observedAt,
        String source
) {
}
