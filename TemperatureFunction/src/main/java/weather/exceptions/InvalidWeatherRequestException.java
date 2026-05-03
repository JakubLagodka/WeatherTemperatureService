package weather.exceptions;

public class InvalidWeatherRequestException extends RuntimeException {
    public InvalidWeatherRequestException(String message) {
        super(message);
    }
}
