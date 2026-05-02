package service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import client.CurrentWeather;
import client.WeatherClient;
import org.junit.jupiter.api.Test;

class WeatherServiceTest {
    @Test
    void returnsStructuredWeatherResponseWithCategory() {
        WeatherClient fakeClient = () -> new CurrentWeather(22.5, "2026-05-01T16:00");
        WeatherService service = new WeatherService(fakeClient, new TemperatureClassifier());

        WeatherResponse response = service.getCurrentWeather();

        assertEquals("Wroclaw", response.city());
        assertEquals(22.5, response.temperatureCelsius());
        assertEquals(TemperatureCategory.WARM, response.category());
        assertEquals("2026-05-01T16:00", response.observedAt());
        assertEquals("Open-Meteo", response.source());
    }
}
