package github.com.ifyosakwe.sky.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.com.ifyosakwe.sky.models.dto.CityDto;
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

    @PostMapping("/current")
    public ResponseEntity<CurrentWeatherApiResponse> getCurrentWeather(
            @RequestBody CityDto cityDto) {

        CurrentWeatherApiResponse response = weatherService.getCurrentWeather(cityDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forecast")
    public ResponseEntity<List<ForecastItemResponse>> getForecast(
            @RequestBody CityDto cityDto) {

        List<ForecastItemResponse> response = weatherService.getForecast(cityDto);
        return ResponseEntity.ok(response);
    }
}
