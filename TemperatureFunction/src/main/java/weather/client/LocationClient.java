package weather.client;

import java.util.Optional;

public interface LocationClient {
    Optional<CityLocation> findLocation(String city);
}
