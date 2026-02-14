package github.com.ifyosakwe.sky.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class WeatherApiException extends RuntimeException {

    private final int statusCode;

    public WeatherApiException(String message) {
        super(message);
        this.statusCode = 503;
    }

    public WeatherApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 503;
    }

    public WeatherApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public WeatherApiException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
