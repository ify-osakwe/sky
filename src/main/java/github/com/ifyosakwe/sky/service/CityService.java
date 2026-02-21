package github.com.ifyosakwe.sky.service;

import github.com.ifyosakwe.sky.exception.BadRequestException;
import github.com.ifyosakwe.sky.exception.CityNotFoundException;
import github.com.ifyosakwe.sky.exception.WeatherApiException;
import github.com.ifyosakwe.sky.models.dto.CityDto;
import github.com.ifyosakwe.sky.models.dto.openweather.GeocodingResponse;
import github.com.ifyosakwe.sky.models.entity.City;
import github.com.ifyosakwe.sky.models.mapper.CityMapper;
import github.com.ifyosakwe.sky.repository.CityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final OpenWeatherMapService openWeatherMapService;
    private final CityMapper cityMapper;

    public CityService(CityRepository cityRepository,
            OpenWeatherMapService openWeatherMapService,
            CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.openWeatherMapService = openWeatherMapService;
        this.cityMapper = cityMapper;
    }

    public List<CityDto> getCities(String city) {
        if (city == null || city.isBlank()) {
            throw new BadRequestException("City name cannot be empty");
        }

        List<GeocodingResponse> geocodingResponses;
        try {
            geocodingResponses = openWeatherMapService.searchCities(city);
        } catch (Exception e) {
            throw new WeatherApiException("Failed to fetch cities", e);
        }

        if (geocodingResponses == null || geocodingResponses.isEmpty()) {
            return List.of();
        }
        return geocodingResponses.stream().map(cityMapper::toCityDto).toList();
    }

    public Optional<City> findByNameAndCountry(String name, String country) {
        return cityRepository.findByNameAndCountry(name, country);
    }

    @Transactional
    public City findOrCreateCity(CityDto cityDto) {
        Optional<City> existingCity = cityRepository.findByNameAndCountry(
                cityDto.getName(),
                cityDto.getCountry());

        if (existingCity.isPresent()) {
            return existingCity.get();
        }

        City city = new City();
        city.setName(cityDto.getName());
        city.setCountry(cityDto.getCountry());
        city.setLatitude(BigDecimal.valueOf(cityDto.getLatitude()));
        city.setLongitude(BigDecimal.valueOf(cityDto.getLongitude()));
        city.setSearchCount(0);

        return cityRepository.save(city);
    }

    @Transactional
    public void incrementSearchCount(Long cityId) {
        if (cityId == null) {
            throw new CityNotFoundException(cityId);
        }
        cityRepository.findById(cityId).ifPresent(city -> {
            city.setSearchCount(city.getSearchCount() + 1);
            city.setLastSearched(LocalDateTime.now());
            cityRepository.save(city);
        });
    }

    public List<City> getRecentlySearchedCities(int limit) {
        if (limit < 1 || limit > 10) {
            throw new BadRequestException("Limit must be between 1 and 10");
        }
        PageRequest pageRequest = PageRequest.of(
                0, limit, Sort.by(Sort.Direction.DESC, "lastSearched"));
        return cityRepository.findByLastSearchedIsNotNull(pageRequest);
    }

}
