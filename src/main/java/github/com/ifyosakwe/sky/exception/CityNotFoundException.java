package github.com.ifyosakwe.sky.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(String cityName) {
        super("City not found: " + cityName);
    }

    public CityNotFoundException(String cityName, Throwable cause) {
        super("City not found: " + cityName, cause);
    }

    public CityNotFoundException(Long cityId) {
        super("City not found with ID: " + cityId);
    }
}
