package github.com.ifyosakwe.sky.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class SkyExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        SkyErrorResponse skyErrorResponse = new SkyErrorResponse(
                "An unexpected error occurred: " + ex.getMessage(),
                new Date());
        return new ResponseEntity<>(skyErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<Object> handleCityNotFoundException(CityNotFoundException ex, WebRequest request) {
        SkyErrorResponse skyErrorResponse = new SkyErrorResponse(
                "City not found: " + ex.getMessage(),
                new Date());
        return new ResponseEntity<>(skyErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WeatherApiException.class)
    public ResponseEntity<Object> handleWeatherApiException(WeatherApiException ex, WebRequest request) {
        SkyErrorResponse skyErrorResponse = new SkyErrorResponse(
                "Weather API error: " + ex.getMessage(),
                new Date());
        return new ResponseEntity<>(skyErrorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        SkyErrorResponse skyErrorResponse = new SkyErrorResponse(
                "Bad request: " + ex.getMessage(),
                new Date());
        return new ResponseEntity<>(skyErrorResponse, HttpStatus.BAD_REQUEST);
    }

}
