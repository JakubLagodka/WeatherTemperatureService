package weather.exceptions;

public class OpenMeteoApiException extends RuntimeException {
    public OpenMeteoApiException(String message) {
        super(message);
    }

    public OpenMeteoApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
