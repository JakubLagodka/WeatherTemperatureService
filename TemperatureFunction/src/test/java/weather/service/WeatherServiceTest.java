package weather.service;

import org.junit.jupiter.api.Test;
import weather.client.LocationClient;
import weather.client.OpenMeteoWeatherClient;
import weather.client.WeatherClient;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherServiceTest {
    @Test
    void returnsStructuredWeatherResponseWithCategory() {
        WeatherClient fakeWeatherClient = new OpenMeteoWeatherClient(HttpClient.newHttpClient());
        LocationClient fakeLocationClient = new OpenMeteoWeatherClient(HttpClient.newHttpClient());
        WeatherService service = new WeatherService(fakeWeatherClient,fakeLocationClient);

        WeatherResponse response = service.getCurrentWeather("Lublin");

        assertEquals("Lublin", response.city());
        assertEquals("Open-Meteo", response.source());
    }
}
