package github.com.ifyosakwe.sky.models.mapper;

import github.com.ifyosakwe.sky.models.dto.openweather.GeocodingResponse;
import github.com.ifyosakwe.sky.models.dto.openweather.WeatherResponse;
import github.com.ifyosakwe.sky.models.entity.City;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CityMapper {

    /**
     * Convert GeocodingResponse to City entity.
     */
    public City toCity(GeocodingResponse dto) {
        City city = new City();
        city.setName(dto.getName());
        city.setCountry(dto.getCountry());
        city.setLatitude(BigDecimal.valueOf(dto.getLatitude()));
        city.setLongitude(BigDecimal.valueOf(dto.getLongitude()));
        city.setSearchCount(0);
        return city;
    }

    /**
     * Convert WeatherResponse (current weather) to City entity.
     * Useful when you get city data from the weather response.
     */
    public City toCity(WeatherResponse dto) {
        City city = new City();
        city.setName(dto.name);

        if (dto.coord != null) {
            city.setLatitude(BigDecimal.valueOf(dto.coord.latitude));
            city.setLongitude(BigDecimal.valueOf(dto.coord.longitude));
        }

        if (dto.sys != null) {
            city.setCountry(dto.sys.country);
        }

        city.setSearchCount(0);
        return city;
    }
}
