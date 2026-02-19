package github.com.ifyosakwe.sky.controller;

import github.com.ifyosakwe.sky.exception.BadRequestException;
import github.com.ifyosakwe.sky.exception.WeatherApiException;
import github.com.ifyosakwe.sky.models.dto.CityDto;
import github.com.ifyosakwe.sky.models.dto.openweather.GeocodingResponse;
import github.com.ifyosakwe.sky.models.dto.response.RecentCitiesResponse;
import github.com.ifyosakwe.sky.models.dto.response.RecentCitiesResponse.RecentCity;
import github.com.ifyosakwe.sky.models.entity.City;
import github.com.ifyosakwe.sky.service.CityService;
import github.com.ifyosakwe.sky.service.OpenWeatherMapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityApiController {

    private final CityService cityService;
    private final OpenWeatherMapService openWeatherMapService;

    public CityApiController(CityService cityService, OpenWeatherMapService openWeatherMapService) {
        this.cityService = cityService;
        this.openWeatherMapService = openWeatherMapService;
    }

    /**
     * Search for cities by name. Always calls the OpenWeatherMap Geocoding API
     * so the user can select the exact city + country they intend.
     */
    @GetMapping("/search")
    public ResponseEntity<List<CityDto>> searchCities(@RequestParam String query) {
        if (query == null || query.isBlank()) {
            throw new BadRequestException("Search query cannot be empty");
        }

        List<GeocodingResponse> results;
        try {
            results = openWeatherMapService.searchCities(query);
        } catch (Exception e) {
            throw new WeatherApiException("Failed to search cities for: " + query);
        }

        List<CityDto> cityDtos = results.stream()
                .map(geo -> new CityDto(
                        geo.getName(),
                        geo.getState(),
                        geo.getCountry(),
                        geo.getLatitude(),
                        geo.getLongitude(),
                        null))
                .toList();

        return ResponseEntity.ok(cityDtos);
    }

    @GetMapping("/recent")
    public ResponseEntity<RecentCitiesResponse> getRecentCities(
            @RequestParam(defaultValue = "10") int limit) {
        List<City> cities = cityService.getRecentlySearchedCities(limit);

        List<RecentCity> recentCities = cities.stream()
                .map(city -> new RecentCity(
                        city.getName(),
                        city.getCountry(),
                        city.getSearchCount(),
                        city.getLastSearched()))
                .toList();

        return ResponseEntity.ok(new RecentCitiesResponse(recentCities));
    }
}
