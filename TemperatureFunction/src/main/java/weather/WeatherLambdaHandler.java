package weather;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import weather.client.LocationClient;
import weather.client.OpenMeteoWeatherClient;
import weather.client.WeatherClient;
import weather.service.WeatherService;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class WeatherLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    WeatherClient weatherClient = new OpenMeteoWeatherClient(HttpClient.newHttpClient());
    LocationClient locationClient = new OpenMeteoWeatherClient(HttpClient.newHttpClient());
    WeatherService weatherService = new WeatherService(weatherClient,locationClient);
    ObjectMapper objectMapper = new ObjectMapper();

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        String city = queryParameters(input).get("city");
        try {
            return response
                    .withStatusCode(200)
                    .withHeaders(headers)
                    .withBody(objectMapper.writeValueAsString(weatherService.getCurrentWeather(city)));
        } catch (JsonProcessingException e) {
            return response
                    .withBody("{\"message\":\"Failed to serialize response\"}")
                    .withHeaders(headers)
                    .withStatusCode(500);
        }
    }

    private Map<String, String> queryParameters(APIGatewayProxyRequestEvent input) {
        if (input == null || input.getQueryStringParameters() == null) {
            return Map.of();
        }
        return input.getQueryStringParameters();
    }
}
