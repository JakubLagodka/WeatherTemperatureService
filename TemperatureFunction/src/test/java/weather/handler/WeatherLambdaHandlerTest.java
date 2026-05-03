package weather.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WeatherLambdaHandlerTest {
  @Test
  public void successfulResponse() {
    WeatherLambdaHandler app = new WeatherLambdaHandler ();
    APIGatewayProxyResponseEvent result = app.handleRequest(new APIGatewayProxyRequestEvent().withQueryStringParameters(Map.of("city","Lublin")), null);
    assertEquals(200, result.getStatusCode().intValue());
    assertEquals("application/json", result.getHeaders().get("Content-Type"));
    String content = result.getBody();
    assertNotNull(content);
  }
}
