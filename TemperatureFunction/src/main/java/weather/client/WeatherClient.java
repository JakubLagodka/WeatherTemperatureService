package weather.client;

import weather.model.CityLocation;
import weather.model.CurrentWeather;

public interface WeatherClient {
    CurrentWeather fetchCurrentWeather(CityLocation location);
}
