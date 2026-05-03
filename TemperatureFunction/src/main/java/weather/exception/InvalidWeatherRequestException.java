package weather.exception;

public class InvalidWeatherRequestException extends RuntimeException {
    public InvalidWeatherRequestException(String message) {
        super(message);
    }
}
