# Wroclaw Weather Lambda

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

## Design Decisions

- `WeatherLambdaHandler` only orchestrates request handling. It wires dependencies and delegates the work.
- `WeatherService` contains business use-case logic: fetch current weather and classify the temperature.
- `TemperatureClassifier` isolates the temperature category rules, keeping them independent from AWS and HTTP concerns.
- `WeatherClient` is an interface for external weather communication.
- `OpenMeteoWeatherClient` is the only class that knows the Open-Meteo endpoint and response format.
- Coordinates are fixed for Wroclaw: latitude `51.1079`, longitude `17.0385`.

Open-Meteo endpoint used:

```text
https://api.open-meteo.com/v1/forecast?latitude=51.1079&longitude=17.0385&current=temperature_2m&timezone=Europe%2FWarsaw
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

## Deploy

Using AWS SAM:

```bash
sam build
sam deploy --guided
```

Or create a Lambda manually in the AWS Console:

- Runtime: `Java 21`
- Handler: `com.example.weather.lambda.WeatherLambdaHandler::handleRequest`
- Artifact: `target/wroclaw-weather-lambda-1.0.0.jar`