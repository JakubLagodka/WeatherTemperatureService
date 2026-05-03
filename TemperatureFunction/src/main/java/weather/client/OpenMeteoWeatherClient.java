package weather.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import weather.exceptions.OpenMeteoApiException;
import weather.exceptions.WeatherApiException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class OpenMeteoWeatherClient implements WeatherClient, LocationClient {
    private static final URI WROCLAW_CURRENT_WEATHER_URI = URI.create(
            "https://api.open-meteo.com/v1/forecast"
                    + "?latitude=51.1079"
                    + "&longitude=17.0385"
                    + "&current=temperature_2m"
                    + "&timezone=Europe%2FWarsaw"
    );
    private static final String FORECAST_URL = "https://api.open-meteo.com/v1/forecast";
    private static final String GEOCODING_URL = "https://geocoding-api.open-meteo.com/v1/search";
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
    public CurrentWeather fetchCurrentWeather(CityLocation location) {
        HttpRequest request = HttpRequest.newBuilder(buildCurrentWeatherUri(location))
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

    @Override
    public Optional<CityLocation> findLocation(String city) {
        URI uri = URI.create(GEOCODING_URL
                + "?name=" + URLEncoder.encode(city, StandardCharsets.UTF_8)
                + "&count=1"
                + "&language=en"
                + "&format=json");
        HttpRequest request = HttpRequest.newBuilder(uri)
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new WeatherApiException("Open-Meteo geocoding returned HTTP " + response.statusCode());
            }
            OpenMeteoGeocodingResponse geocodingResponse =
                    objectMapper.readValue(response.body(), OpenMeteoGeocodingResponse.class);
            if (geocodingResponse.results() == null || geocodingResponse.results().isEmpty()) {
                return Optional.empty();
            }

            LocationResult locationResult = geocodingResponse.results().getFirst();
            if (locationResult == null) {
                return Optional.empty();
            }

            return Optional.of(new CityLocation(
                    locationResult.name(),
                    locationResult.country(),
                    locationResult.latitude(),
                    locationResult.longitude(),
                    locationResult.timezone()
            ));
        } catch (IOException e) {
            throw new WeatherApiException("Failed to read Open-Meteo geocoding response", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WeatherApiException("Open-Meteo geocoding request was interrupted", e);
        }
    }

    private URI buildCurrentWeatherUri(CityLocation location) {
        return URI.create(FORECAST_URL
                + "?latitude=" + location.latitude()
                + "&longitude=" + location.longitude()
                + "&current=temperature_2m"
                + "&timezone=" + URLEncoder.encode(location.timezone(), StandardCharsets.UTF_8));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record OpenMeteoResponse(Current current) {
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record OpenMeteoGeocodingResponse(List<LocationResult> results) {
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Current(String time,
                           @com.fasterxml.jackson.annotation.JsonProperty("temperature_2m") double temperature2m) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record LocationResult(
            String name,
            String country,
            double latitude,
            double longitude,
            String timezone
    ) {
    }
}
