package github.com.ifyosakwe.sky.service;

import github.com.ifyosakwe.sky.exception.CityNotFoundException;
import github.com.ifyosakwe.sky.models.dto.CityDto;
import github.com.ifyosakwe.sky.models.dto.openweather.ForecastResponse;
import github.com.ifyosakwe.sky.models.dto.openweather.GeocodingResponse;
import github.com.ifyosakwe.sky.models.dto.openweather.WeatherResponse;
import github.com.ifyosakwe.sky.models.dto.response.CurrentWeatherApiResponse;
import github.com.ifyosakwe.sky.models.dto.response.ForecastItemResponse;
import github.com.ifyosakwe.sky.models.entity.City;
import github.com.ifyosakwe.sky.models.entity.CurrentWeather;
import github.com.ifyosakwe.sky.models.entity.Forecast;
import github.com.ifyosakwe.sky.models.mapper.CityMapper;
import github.com.ifyosakwe.sky.models.mapper.WeatherMapper;
import github.com.ifyosakwe.sky.repository.CurrentWeatherRepository;
import github.com.ifyosakwe.sky.repository.ForecastRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {

    private final OpenWeatherMapService openWeatherMapService;
    private final CityService cityService;
    private final CurrentWeatherRepository currentWeatherRepository;
    private final ForecastRepository forecastRepository;
    private final WeatherMapper weatherMapper;
    private final CityMapper cityMapper;

    public WeatherService(OpenWeatherMapService openWeatherMapService,
            CityService cityService,
            CurrentWeatherRepository currentWeatherRepository,
            ForecastRepository forecastRepository,
            WeatherMapper weatherMapper,
            CityMapper cityMapper) {
        this.openWeatherMapService = openWeatherMapService;
        this.cityService = cityService;
        this.currentWeatherRepository = currentWeatherRepository;
        this.forecastRepository = forecastRepository;
        this.weatherMapper = weatherMapper;
        this.cityMapper = cityMapper;
    }

    /**
     * Get current weather for a city by name (legacy/backward-compatible).
     * Resolves the city, checks for fresh cached data, fetches from API if stale,
     * persists the result, and returns a response DTO.
     */
    @Transactional
    public CurrentWeatherApiResponse getCurrentWeather(String cityName) {
        City city = resolveCity(cityName);
        cityService.incrementSearchCount(city.getId());

        return fetchAndCacheCurrentWeather(city);
    }

    @Transactional
    public CurrentWeatherApiResponse xGetCurrentWeather(CityDto cityDto) {
        Optional<City> cityDB = cityService.xFindByNameAndCountry(
                cityDto.getName(), cityDto.getCountry());
        if (cityDB.isPresent() && isFresh(cityDB.get().getCurrentWeather().getLastUpdated())) {
            cityService.incrementSearchCount(cityDB.get().getId());
            return weatherMapper.toCurrentWeatherResponse(cityDB.get());
        }

        City city = cityService.xFindOrCreateCity(cityDto);
        Optional<CurrentWeather> weatherDB = currentWeatherRepository.findByCityId(city.getId());
        if (weatherDB.isPresent() && isFresh(weatherDB.get().getLastUpdated())) {
            cityService.incrementSearchCount(city.getId());
            return weatherMapper.toCurrentWeatherResponse(weatherDB.get(), city);
        }

        WeatherResponse weatherResponse = openWeatherMapService.fetchCurrentWeather(
                cityDto.getLatitude(),
                cityDto.getLongitude());
        CurrentWeather weather = weatherMapper.toCurrentWeather(weatherResponse, city);

        if (weatherDB.isPresent()) {
            weather.setId(weatherDB.get().getId());
        }
        currentWeatherRepository.save(weather);
        return weatherMapper.toCurrentWeatherResponse(weather, city);
    }

    /**
     * Get current weather using latitude/longitude with explicit city + country.
     * This is the new flow: user searched cities, selected one, and we use its
     * coordinates. The city is resolved by name+country (or created if new).
     */
    @Transactional
    public CurrentWeatherApiResponse getCurrentWeather(double lat, double lon,
            String cityName, String country) {
        City city = cityService.findOrCreateCity(cityName, country, lat, lon);
        cityService.incrementSearchCount(city.getId());

        return fetchAndCacheCurrentWeather(city);
    }

    /**
     * Get forecast for a city by name (legacy/backward-compatible).
     */
    @Transactional
    public List<ForecastItemResponse> getForecast(String cityName) {
        City city = resolveCity(cityName);
        cityService.incrementSearchCount(city.getId());

        return fetchAndCacheForecast(city);
    }

    /**
     * Get forecast using latitude/longitude with explicit city + country.
     */
    @Transactional
    public List<ForecastItemResponse> getForecast(double lat, double lon,
            String cityName, String country) {
        City city = cityService.findOrCreateCity(cityName, country, lat, lon);
        cityService.incrementSearchCount(city.getId());

        return fetchAndCacheForecast(city);
    }

    /**
     * Force-refresh all weather data (current + forecast) for a specific city.
     * Used by the scheduler to keep frequently searched cities up to date.
     */
    @Transactional
    public void refreshWeatherData(City city) {
        // Refresh current weather
        WeatherResponse weatherResponse = openWeatherMapService.fetchCurrentWeather(
                city.getLatitude().doubleValue(),
                city.getLongitude().doubleValue());
        CurrentWeather weather = weatherMapper.toCurrentWeather(weatherResponse, city);

        Optional<CurrentWeather> existing = currentWeatherRepository.findByCityId(city.getId());
        existing.ifPresent(cw -> weather.setId(cw.getId()));
        currentWeatherRepository.save(weather);

        // Refresh forecasts
        ForecastResponse forecastResponse = openWeatherMapService.fetchForecast(
                city.getLatitude().doubleValue(),
                city.getLongitude().doubleValue());
        forecastRepository.deleteByCityId(city.getId());
        forecastRepository.saveAll(weatherMapper.toForecasts(forecastResponse, city));
    }

    // ---- Private helpers ----

    private CurrentWeatherApiResponse fetchAndCacheCurrentWeather(City city) {
        Optional<CurrentWeather> existing = currentWeatherRepository.findByCityId(city.getId());
        if (existing.isPresent() && isFresh(existing.get().getLastUpdated())) {
            return weatherMapper.toCurrentWeatherResponse(existing.get(), city);
        }

        WeatherResponse apiResponse = openWeatherMapService.fetchCurrentWeather(
                city.getLatitude().doubleValue(),
                city.getLongitude().doubleValue());

        CurrentWeather weather = weatherMapper.toCurrentWeather(apiResponse, city);
        if (existing.isPresent()) {
            weather.setId(existing.get().getId());
        }
        currentWeatherRepository.save(weather);

        return weatherMapper.toCurrentWeatherResponse(weather, city);
    }

    private List<ForecastItemResponse> fetchAndCacheForecast(City city) {
        List<Forecast> existing = forecastRepository.findByCityIdAndForecastDateAfter(
                city.getId(), LocalDateTime.now());
        if (!existing.isEmpty() && isFresh(existing.get(0).getForecastDate())) {
            return existing.stream()
                    .map(weatherMapper::toForecastItemResponse)
                    .toList();
        }

        ForecastResponse apiResponse = openWeatherMapService.fetchForecast(
                city.getLatitude().doubleValue(),
                city.getLongitude().doubleValue());

        forecastRepository.deleteByCityId(city.getId());
        List<Forecast> forecasts = weatherMapper.toForecasts(apiResponse, city);
        forecastRepository.saveAll(forecasts);

        return forecasts.stream()
                .map(weatherMapper::toForecastItemResponse)
                .toList();
    }

    private City resolveCity(String cityName) {
        Optional<City> cityFromDB = cityService.findByName(cityName);
        if (cityFromDB.isPresent()) {
            return cityFromDB.get();
        }

        List<GeocodingResponse> results = openWeatherMapService.searchCities(cityName);
        if (results == null || results.isEmpty()) {
            throw new CityNotFoundException("City not found: " + cityName);
        }

        return cityService.findOrCreateCity(results.get(0));
    }

    private boolean isFresh(LocalDateTime lastUpdated) {
        return lastUpdated != null && lastUpdated.isAfter(LocalDateTime.now().minusHours(2));
    }

}
