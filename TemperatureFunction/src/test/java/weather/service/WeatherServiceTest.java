package weather.service;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;
import weather.client.CurrentWeather;
import weather.client.WeatherClient;

class WeatherServiceTest {
    @Test
    void returnsStructuredWeatherResponseWithCategory() {
        WeatherClient fakeClient = () -> new CurrentWeather(22.5, "2026-05-01T16:00");
        WeatherService service = new WeatherService(fakeClient);

        WeatherResponse response = service.getCurrentWeather();

        assertEquals("Wroclaw", response.city());
        assertEquals("22.5", response.temperatureCelsius());
        assertEquals(TemperatureCategory.WARM.toString(), response.category());
        assertEquals("2026-05-01T16:00", response.observedAt());
        assertEquals("Open-Meteo", response.source());
    }
}
