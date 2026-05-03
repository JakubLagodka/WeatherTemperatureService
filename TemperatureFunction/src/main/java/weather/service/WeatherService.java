package weather.service;

import weather.client.CurrentWeather;
import weather.client.WeatherClient;

public class WeatherService {
    private static final String CITY = "Wroclaw";
    private static final String SOURCE = "Open-Meteo";

    private final WeatherClient weatherClient;
    private final TemperatureClassifier temperatureClassifier;

    public WeatherService(WeatherClient weatherClient, TemperatureClassifier temperatureClassifier) {
        this.weatherClient = weatherClient;
        this.temperatureClassifier = temperatureClassifier;
    }

    public WeatherResponse getCurrentWeather() {
        CurrentWeather currentWeather = weatherClient.fetchCurrentWeather();
        TemperatureCategory category = temperatureClassifier.classify(currentWeather.temperatureCelsius());

        return new WeatherResponse(
                CITY,
                currentWeather.temperatureCelsius(),
                category,
                currentWeather.observedAt(),
                SOURCE
        );
    }
}
