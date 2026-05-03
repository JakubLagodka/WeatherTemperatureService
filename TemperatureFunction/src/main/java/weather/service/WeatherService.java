package weather.service;

import weather.client.CurrentWeather;
import weather.client.WeatherClient;

public class WeatherService {
    private static final String CITY = "Wroclaw";
    private static final String SOURCE = "Open-Meteo";

    private final WeatherClient weatherClient;

    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public WeatherResponse getCurrentWeather() {
        CurrentWeather currentWeather = weatherClient.fetchCurrentWeather();
        TemperatureCategory category = TemperatureClassifier.classify(currentWeather.temperatureCelsius());

        return new WeatherResponse(
                CITY,
                String.valueOf(currentWeather.temperatureCelsius()),
                category.toString(),
                currentWeather.observedAt(),
                SOURCE
        );
    }
}
