package github.com.ifyosakwe.sky.controller;

import github.com.ifyosakwe.sky.exception.BadRequestException;
import github.com.ifyosakwe.sky.models.dto.CityDto;
import github.com.ifyosakwe.sky.models.entity.City;
import github.com.ifyosakwe.sky.service.CityService;
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

    public CityApiController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<CityDto>> searchCities(@RequestParam String query) {
        if (query == null || query.isBlank()) {
            throw new BadRequestException("Search query cannot be empty");
        }

        List<CityDto> cityDtos = cityService.getCities(query);
        return ResponseEntity.ok(cityDtos);
    }

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
