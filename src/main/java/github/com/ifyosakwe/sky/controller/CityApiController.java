package github.com.ifyosakwe.sky.controller;

import github.com.ifyosakwe.sky.exception.BadRequestException;
import github.com.ifyosakwe.sky.models.dto.skyapi.CityDto;
import github.com.ifyosakwe.sky.models.entity.City;
import github.com.ifyosakwe.sky.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "City")
@RestController
@RequestMapping("/api/cities")
public class CityApiController {

    private final CityService cityService;

    public CityApiController(CityService cityService) {
        this.cityService = cityService;
    }

    @Operation(summary = "Search for a city by name")
    @GetMapping("/search")
    public ResponseEntity<List<CityDto>> searchCities(@RequestParam String query) {
        if (query == null || query.isBlank()) {
            throw new BadRequestException("Search query cannot be empty");
        }

        List<CityDto> cityDtos = cityService.getCities(query);
        return ResponseEntity.ok(cityDtos);
    }

    @Operation(summary = "Get the most recently searched cities")
    @GetMapping("/recent")
    public ResponseEntity<List<CityDto>> getRecentCities(
            @RequestParam(defaultValue = "10") int limit) {
        List<City> cities = cityService.getRecentlySearchedCities(limit);

        List<CityDto> recentCities = cities.stream()
                .map(city -> new CityDto(
                        city.getName(),
                        city.getCountry(),
                        city.getLatitude().doubleValue(),
                        city.getLongitude().doubleValue()))
                .toList();

        return ResponseEntity.ok(recentCities);
    }
}
