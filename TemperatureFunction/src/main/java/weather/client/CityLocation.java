package weather.client;

public record CityLocation(
        String name,
        String country,
        double latitude,
        double longitude,
        String timezone
) {
}
