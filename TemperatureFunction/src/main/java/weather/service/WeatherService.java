package weather.service;

import weather.client.CityLocation;
import weather.client.CurrentWeather;
import weather.client.LocationClient;
import weather.client.WeatherClient;
import weather.exceptions.CityNotFoundException;
import weather.exceptions.InvalidWeatherRequestException;

public class WeatherService {
    private static final String SOURCE = "Open-Meteo";

    private final WeatherClient weatherClient;
    private final LocationClient locationClient;

    public WeatherService(WeatherClient weatherClient, LocationClient locationClient) {
        this.weatherClient = weatherClient;
        this.locationClient = locationClient;
    }

    public WeatherResponse getCurrentWeather(String city) {
        if (city == null || city.isBlank()) {
            throw new InvalidWeatherRequestException("Query parameter 'city' is required");
        }
        CityLocation location = locationClient.findLocation(city.trim())
                .orElseThrow(() -> new CityNotFoundException("City not found: " + city.trim()));
        CurrentWeather currentWeather = weatherClient.fetchCurrentWeather(location);
        TemperatureCategory category = TemperatureClassifier.classify(currentWeather.temperatureCelsius());

        return new WeatherResponse(
                city,
                String.valueOf(currentWeather.temperatureCelsius()),
                category.toString(),
                currentWeather.observedAt(),
                SOURCE
        );
    }
}
