package weather;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WeatherLambdaHandlerTest {
  @Test
  public void successfulResponse() {
    WeatherLambdaHandler  app = new WeatherLambdaHandler ();
    APIGatewayProxyResponseEvent result = app.handleRequest(null, null);
    assertEquals(200, result.getStatusCode().intValue());
    assertEquals("application/json", result.getHeaders().get("Content-Type"));
    String content = result.getBody();
    assertNotNull(content);
  }
}
