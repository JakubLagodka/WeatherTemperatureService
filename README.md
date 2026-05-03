# Weather Temperature Service using AWS Lambda

The AWS Lambda function written using Java 21 language returns the current temperature in city given by city name as a parameter using the Open-Meteo Forecast API.

The Lambda returns structured JSON:

```json
{
  "city": "Wroclaw",
  "temperatureCelsius": 18.4,
  "category": "Mild",
  "observedAt": "2026-05-01T15:30",
  "source": "Open-Meteo"
}
```
## Brief Description of the solution

-  Coordinates were firstly fixed for Wroclaw: latitude `51.1079`, longitude `17.0385`, later expended that the city name is provided as a GET query parameter.
- `WeatherLambdaHandler` only orchestrates request handling. It wires dependencies and delegates the work.
- `WeatherService` contains business use-case logic: fetch current weather and classify the temperature.
- `TemperatureClassifier` isolates the temperature category rules, keeping them independent from AWS and HTTP concerns.
- `WeatherClient` is an interface for external weather communication.
- `LocationClient` is an interface for external weather communication.
- `OpenMeteoWeatherClient` is the only class that knows the Open-Meteo endpoint and response format.


## Design Decisions

I have firstly used Open-Meteo endpoint for Wrocław:

```text
https://api.open-meteo.com/v1/forecast?latitude=51.1079&longitude=17.0385&current=temperature_2m&timezone=Europe%2FWarsaw
```

Open-Meteo endpoints used for extending the task:

```text
https://geocoding-api.open-meteo.com/v1/search?name={city}&count=1&language=en&format=json
https://api.open-meteo.com/v1/forecast?latitude={latitude}&longitude={longitude}&current=temperature_2m&timezone={timezone}
```

The Open-Meteo documentation states that `/v1/forecast` accepts `latitude`, `longitude`, and a `current` parameter for current weather variables, and returns JSON.

## Temperature Categories

| Temperature range | Category |
| --- | --- |
| `< 0 deg C` | Freezing |
| `0-10 deg C` | Cold |
| `10-20 deg C` | Mild |
| `20-30 deg C` | Warm |
| `> 30 deg C` | Hot |

## Build

```bash
mvn clean package
```

The deployable shaded JAR is created under `target/`.

## AWS Lambda Configuration

- Runtime: `Java 21`
- Handler: `weather.handler.WeatherLambdaHandler::handleRequest`
- API path: `GET /temperature?city=[city_name]`

## Deploy With AWS SAM

```bash
sam build
sam deploy --guided
sam local start-api
```

## Unit Testing

Business logic can be tested with fake `LocationClient` and `WeatherClient` implementations. This avoids network calls and keeps tests deterministic. `TemperatureClassifierTest` verifies category boundaries directly, `WeatherServiceTest` verifies city-aware service behavior, and `WeatherLambdaHandlerTest` verifies API Gateway query parameter orchestration.

Run tests:

```bash
mvn test
```