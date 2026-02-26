package github.com.ifyosakwe.sky.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import github.com.ifyosakwe.sky.models.dto.skyapi.CityDto;
import github.com.ifyosakwe.sky.models.dto.skyapi.CurrentWeatherResponse;
import github.com.ifyosakwe.sky.models.dto.skyapi.ForecastItemResponse;
import github.com.ifyosakwe.sky.models.entity.City;
import github.com.ifyosakwe.sky.service.CityService;
import github.com.ifyosakwe.sky.service.WeatherService;

@Controller
public class WeatherWebController {

    private final WeatherService weatherService;
    private final CityService cityService;

    public WeatherWebController(WeatherService weatherService, CityService cityService) {
        this.weatherService = weatherService;
        this.cityService = cityService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<City> recentCities = cityService.getRecentlySearchedCities(5);
        model.addAttribute("recentCities", recentCities);
        return "home";
    }

    @GetMapping("/search")
    public String search(@RequestParam String query, Model model) {
        List<CityDto> cities = cityService.getCities(query);
        model.addAttribute("cities", cities);
        model.addAttribute("query", query);
        if (cities.size() == 1) {
            CityDto city = cities.get(0);
            return "redirect:/weather?name=" + city.getName()
                    + "&country=" + city.getCountry()
                    + "&lat=" + city.getLatitude()
                    + "&lon=" + city.getLongitude()
                    + "&state=" + (city.getState() != null ? city.getState() : "");
        }
        return "search";
    }

    @GetMapping("/weather")
    public String weather(
            @RequestParam String name,
            @RequestParam String country,
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(required = false) String state,
            Model model) {
        CityDto cityDto = new CityDto(name, state, country, lat, lon);
        CurrentWeatherResponse currentWeather = weatherService.getCurrentWeather(cityDto);
        List<ForecastItemResponse> forecast = weatherService.getForecast(cityDto);
        model.addAttribute("weather", currentWeather);
        model.addAttribute("forecast", forecast);
        model.addAttribute("city", cityDto);
        return "weather";
    }

    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}
