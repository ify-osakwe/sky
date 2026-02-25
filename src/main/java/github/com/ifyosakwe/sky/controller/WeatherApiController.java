package github.com.ifyosakwe.sky.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.com.ifyosakwe.sky.models.dto.skyapi.CityDto;
import github.com.ifyosakwe.sky.models.dto.skyapi.CurrentWeatherResponse;
import github.com.ifyosakwe.sky.models.dto.skyapi.ForecastItemResponse;
import github.com.ifyosakwe.sky.service.WeatherService;

@RestController
@RequestMapping("/api/weather")
public class WeatherApiController {

    private final WeatherService weatherService;

    public WeatherApiController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public ResponseEntity<CurrentWeatherResponse> getCurrentWeather(
            CityDto cityDto) {

        CurrentWeatherResponse response = weatherService.getCurrentWeather(cityDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/forecast")
    public ResponseEntity<List<ForecastItemResponse>> getForecast(
            CityDto cityDto) {

        List<ForecastItemResponse> response = weatherService.getForecast(cityDto);
        return ResponseEntity.ok(response);
    }
}
