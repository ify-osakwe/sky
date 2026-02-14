package github.com.ifyosakwe.sky.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import github.com.ifyosakwe.sky.models.dto.openweather.ForecastResponse;
import github.com.ifyosakwe.sky.models.dto.openweather.GeocodingResponse;
import github.com.ifyosakwe.sky.models.dto.openweather.WeatherResponse;

@SpringBootTest
public class OpenWeatherMapServiceIntegrationTest {
    @Autowired
    private OpenWeatherMapService openWeatherMapService;

    @Test
    void shouldSearchCities() {
        List<GeocodingResponse> cities = openWeatherMapService.searchCities("London");

        assertNotNull(cities);
        assertFalse(cities.isEmpty());
        System.out.println("Found city: " + cities.get(0).getName() + ", " + cities.get(0).getCountry());
    }

    @Test
    void shouldFetchCurrentWeather() {
        // London coordinates
        WeatherResponse weather = openWeatherMapService.fetchCurrentWeather(51.5074, -0.1278);

        assertNotNull(weather);
        assertNotNull(weather.main);
        System.out.println("Temperature: " + weather.main.temp + "K");
        System.out.println("Weather: " + weather.weather.get(0).description);
    }

    @Test
    void shouldFetchForecast() {
        ForecastResponse forecast = openWeatherMapService.fetchForecast(51.5074, -0.1278);

        assertNotNull(forecast);
        assertNotNull(forecast.list);
        assertFalse(forecast.list.isEmpty());
        System.out.println("Forecast items: " + forecast.list.size());
    }

}
