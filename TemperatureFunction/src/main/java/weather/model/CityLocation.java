package weather.model;

public record CityLocation(
        String name,
        String country,
        double latitude,
        double longitude,
        String timezone
) {
}
