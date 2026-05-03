package weather.service;

import org.junit.jupiter.api.Test;
import weather.client.LocationClient;
import weather.client.OpenMeteoClient;
import weather.client.WeatherClient;
import weather.model.WeatherResponse;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherServiceTest {
    @Test
    void returnsStructuredWeatherResponseWithCategory() {
        WeatherClient fakeWeatherClient = new OpenMeteoClient(HttpClient.newHttpClient());
        LocationClient fakeLocationClient = new OpenMeteoClient(HttpClient.newHttpClient());
        WeatherService service = new WeatherService(fakeWeatherClient,fakeLocationClient);

        WeatherResponse response = service.getCurrentWeather("Lublin");

        assertEquals("Lublin", response.city());
        assertEquals("Open-Meteo", response.source());
    }
}
