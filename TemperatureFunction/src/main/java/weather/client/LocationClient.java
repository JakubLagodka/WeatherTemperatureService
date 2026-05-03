package weather.client;

import weather.model.CityLocation;

import java.util.Optional;

public interface LocationClient {
    Optional<CityLocation> findLocation(String city);
}
