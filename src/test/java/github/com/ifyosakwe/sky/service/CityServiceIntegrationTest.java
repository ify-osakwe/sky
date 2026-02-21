package github.com.ifyosakwe.sky.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import github.com.ifyosakwe.sky.models.dto.openweather.GeocodingResponse;
import github.com.ifyosakwe.sky.models.entity.City;

@SpringBootTest
public class CityServiceIntegrationTest {

    @Autowired
    private CityService cityService;

    @Autowired
    private OpenWeatherMapService openWeatherMapService;

    @Test
    void shouldCreateAndTrackCity() {
        // 1. Search for a city
        List<GeocodingResponse> results = openWeatherMapService.searchCities("Lagos");
        GeocodingResponse lagosData = results.get(0);

        // 2. Create or find city in database
        // City city = cityService.findOrCreateCity(lagosData);
        // assertNotNull(city.getId());

        // 3. Increment search count
        // cityService.xIncrementSearchCount(city.getId());

        // 4. Verify it was saved
        // City fetched = cityService.findById(city.getId()).orElseThrow();
        // assertEquals(1, fetched.getSearchCount());
    }
}
