package github.com.ifyosakwe.sky.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import github.com.ifyosakwe.sky.exception.BadRequestException;
import github.com.ifyosakwe.sky.models.dto.response.CurrentWeatherApiResponse;
import github.com.ifyosakwe.sky.models.dto.response.ForecastItemResponse;
import github.com.ifyosakwe.sky.service.WeatherService;

@RestController
@RequestMapping("/api/weather")
public class WeatherApiController {

    private final WeatherService weatherService;

    public WeatherApiController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Get current weather.
     * - New flow: ?lat={lat}&lon={lon}&city={cityName}&country={countryCode}
     * - Legacy flow: ?city={cityName}
     */
    @GetMapping("/current")
    public ResponseEntity<CurrentWeatherApiResponse> getCurrentWeather(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String lat,
            @RequestParam(required = false) String lon,
            @RequestParam(required = false) String country) {

        if (lat != null && lon != null && city != null && country != null) {
            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(lon);
            CurrentWeatherApiResponse response = weatherService.getCurrentWeather(
                    latitude, longitude, city, country);
            return ResponseEntity.ok(response);
        }

        if (city != null && !city.isBlank()) {
            CurrentWeatherApiResponse response = weatherService.getCurrentWeather(city);
            return ResponseEntity.ok(response);
        }

        throw new BadRequestException(
                "Provide either 'city' alone, or 'lat', 'lon', 'city', and 'country' together");
    }

    /**
     * Get forecast.
     * - New flow: ?lat={lat}&lon={lon}&city={cityName}&country={countryCode}
     * - Legacy flow: ?city={cityName}
     */
    @GetMapping("/forecast")
    public ResponseEntity<List<ForecastItemResponse>> getForecast(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String lat,
            @RequestParam(required = false) String lon,
            @RequestParam(required = false) String country) {

        if (lat != null && lon != null && city != null && country != null) {
            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(lon);
            List<ForecastItemResponse> response = weatherService.getForecast(
                    latitude, longitude, city, country);
            return ResponseEntity.ok(response);
        }

        if (city != null && !city.isBlank()) {
            List<ForecastItemResponse> response = weatherService.getForecast(city);
            return ResponseEntity.ok(response);
        }

        throw new BadRequestException(
                "Provide either 'city' alone, or 'lat', 'lon', 'city', and 'country' together");
    }
}
