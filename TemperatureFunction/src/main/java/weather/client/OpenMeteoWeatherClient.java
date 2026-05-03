package weather.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OpenMeteoWeatherClient implements WeatherClient {
    private static final URI WROCLAW_CURRENT_WEATHER_URI = URI.create(
            "https://api.open-meteo.com/v1/forecast"
                    + "?latitude=51.1079"
                    + "&longitude=17.0385"
                    + "&current=temperature_2m"
                    + "&timezone=Europe%2FWarsaw"
    );

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OpenMeteoWeatherClient(HttpClient httpClient) {
        this(httpClient, new ObjectMapper());
    }

    OpenMeteoWeatherClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public CurrentWeather fetchCurrentWeather() {
        HttpRequest request = HttpRequest.newBuilder(WROCLAW_CURRENT_WEATHER_URI)
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new OpenMeteoApiException("Open-Meteo returned HTTP " + response.statusCode());
            }

            OpenMeteoResponse openMeteoResponse = objectMapper.readValue(response.body(), OpenMeteoResponse.class);
            if (openMeteoResponse.current() == null) {
                throw new OpenMeteoApiException("Open-Meteo response does not contain current weather data");
            }

            return new CurrentWeather(
                    openMeteoResponse.current().temperature2m,
                    openMeteoResponse.current().time()
            );
        } catch (IOException e) {
            throw new OpenMeteoApiException("Failed to read Open-Meteo response", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OpenMeteoApiException("Open-Meteo request was interrupted", e);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record OpenMeteoResponse(Current current) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Current(String time,
                           @com.fasterxml.jackson.annotation.JsonProperty("temperature_2m") double temperature2m) {
    }
}
