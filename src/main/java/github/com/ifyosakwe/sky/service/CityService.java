package github.com.ifyosakwe.sky.service;

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

    public List<CityDto> xGetCities(String city) {
        List<GeocodingResponse> geocodingResponses = openWeatherMapService.searchCities(city);
        return geocodingResponses.stream().map(cityMapper::toCityDto).toList();
    }

    public Optional<City> xFindByNameAndCountry(String name, String country) {
        return cityRepository.findByNameAndCountry(name, country);
    }

    @Transactional
    public City xFindOrCreateCity(CityDto cityDto) {
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
    public City findOrCreateCity(GeocodingResponse geocodingData) {
        Optional<City> existingCity = cityRepository.findByNameAndCountry(
                geocodingData.getName(),
                geocodingData.getCountry());

        if (existingCity.isPresent()) {
            return existingCity.get();
        }

        City city = new City();
        city.setName(geocodingData.getName());
        city.setCountry(geocodingData.getCountry());
        city.setLatitude(BigDecimal.valueOf(geocodingData.getLatitude()));
        city.setLongitude(BigDecimal.valueOf(geocodingData.getLongitude()));
        city.setSearchCount(0);

        return cityRepository.save(city);
    }

    public Optional<City> findByName(String name) {
        return cityRepository.findByNameIgnoreCase(name);
    }

    

    @Transactional
    public City findOrCreateCity(String name, String country, double lat, double lon) {
        Optional<City> existingCity = cityRepository.findByNameAndCountry(name, country);
        if (existingCity.isPresent()) {
            return existingCity.get();
        }

        City city = new City();
        city.setName(name);
        city.setCountry(country);
        city.setLatitude(BigDecimal.valueOf(lat));
        city.setLongitude(BigDecimal.valueOf(lon));
        city.setSearchCount(0);

        return cityRepository.save(city);
    }

    @Transactional
    public void incrementSearchCount(Long cityId) {
        cityRepository.findById(cityId).ifPresent(city -> {
            city.setSearchCount(city.getSearchCount() + 1);
            city.setLastSearched(LocalDateTime.now());
            cityRepository.save(city);
        });
    }

    public List<City> getRecentlySearchedCities(int limit) {
        PageRequest pageRequest = PageRequest.of(
                0, limit, Sort.by(Sort.Direction.DESC, "lastSearched"));
        return cityRepository.findByLastSearchedIsNotNull(pageRequest);
    }

    /**
     * Find a city by ID.
     */
    // public Optional<City> findById(Long id) {
    // return cityRepository.findById(id);
    // }

    /**
     * Get cities ordered by search count (most popular first).
     */
    // public List<City> getMostSearchedCities(int limit) {
    // PageRequest pageRequest = PageRequest.of(0, limit,
    // Sort.by(Sort.Direction.DESC, "searchCount"));
    // return cityRepository.findAll(pageRequest).getContent();
    // }

    /**
     * Search cities by partial name (for autocomplete).
     */
    // public List<City> searchCities(String query) {
    // return cityRepository.findByNameContainingIgnoreCase(query);
    // }

    /**
     * Save a city entity.
     */
    // @Transactional
    // public City save(City city) {
    // return cityRepository.save(city);
    // }
}
