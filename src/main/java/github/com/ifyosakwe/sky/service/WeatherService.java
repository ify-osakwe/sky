package github.com.ifyosakwe.sky.service;

import github.com.ifyosakwe.sky.models.dto.CityDto;
import github.com.ifyosakwe.sky.models.dto.openweather.WeatherResponse;
import github.com.ifyosakwe.sky.models.dto.openweather.ForecastResponse.ForecastItem;
import github.com.ifyosakwe.sky.models.dto.response.CurrentWeatherApiResponse;
import github.com.ifyosakwe.sky.models.dto.response.ForecastItemResponse;
import github.com.ifyosakwe.sky.models.entity.City;
import github.com.ifyosakwe.sky.models.entity.CurrentWeather;
import github.com.ifyosakwe.sky.models.entity.Forecast;
import github.com.ifyosakwe.sky.models.mapper.WeatherMapper;
import github.com.ifyosakwe.sky.repository.CurrentWeatherRepository;
import github.com.ifyosakwe.sky.repository.ForecastRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    private final OpenWeatherMapService openWeatherMapService;
    private final CityService cityService;
    private final CurrentWeatherRepository currentWeatherRepository;
    private final ForecastRepository forecastRepository;
    private final WeatherMapper weatherMapper;

    public WeatherService(OpenWeatherMapService openWeatherMapService,
            CityService cityService,
            CurrentWeatherRepository currentWeatherRepository,
            ForecastRepository forecastRepository,
            WeatherMapper weatherMapper) {
        this.openWeatherMapService = openWeatherMapService;
        this.cityService = cityService;
        this.currentWeatherRepository = currentWeatherRepository;
        this.forecastRepository = forecastRepository;
        this.weatherMapper = weatherMapper;

    }

    @Transactional
    public CurrentWeatherApiResponse getCurrentWeather(CityDto cityDto) {
        Optional<City> cityDB = cityService.findByNameAndCountry(
                cityDto.getName(), cityDto.getCountry());
        if (cityDB.isPresent()
                && cityDB.get().getCurrentWeather() != null
                && isFresh(cityDB.get().getCurrentWeather().getLastUpdated())) {
            cityService.incrementSearchCount(cityDB.get().getId());
            log.debug("Getting Weather from DB: {}", cityDto.getFullname());
            return weatherMapper.toCurrentWeatherResponse(cityDB.get());
        }

        City city = cityService.findOrCreateCity(cityDto);
        Optional<CurrentWeather> weatherDB = currentWeatherRepository.findByCityId(city.getId());
        if (weatherDB.isPresent() && isFresh(weatherDB.get().getLastUpdated())) {
            cityService.incrementSearchCount(city.getId());
            log.debug("Getting Weather from DB II: {}", cityDto.getFullname());
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
        log.debug("Getting Weather from API: {}", cityDto.getFullname());
        return weatherMapper.toCurrentWeatherResponse(weather, city);
    }

    @Transactional
    public List<ForecastItemResponse> getForecast(CityDto cityDto) {
        Optional<City> cityDB = cityService.findByNameAndCountry(
                cityDto.getName(), cityDto.getCountry());
        if (cityDB.isPresent()) {
            List<Forecast> forcastListDB = forecastRepository.findByCityIdAndForecastDateAfter(
                    cityDB.get().getId(), LocalDateTime.now());
            if (!forcastListDB.isEmpty() && isFresh(forcastListDB.get(0).getForecastDate())) {
                log.debug("Getting Forecast from DB: {}", cityDto.getFullname());
                return forcastListDB.stream()
                        .map(weatherMapper::toForecastItemResponse)
                        .toList();
            }
        }

        City city = cityDB.isPresent() ? cityDB.get() : cityService.findOrCreateCity(cityDto);
        cityService.incrementSearchCount(city.getId());
        List<ForecastItem> forecastListAPI = openWeatherMapService.fetchDailyForecast(
                cityDto.getLatitude(),
                cityDto.getLongitude());

        forecastRepository.deleteByCityId(city.getId());
        List<Forecast> forecastList = weatherMapper.toForecasts(forecastListAPI, city);
        forecastRepository.saveAll(forecastList);

        log.debug("Getting Forecast from API: {}", cityDto.getFullname());
        return forecastList.stream()
                .map(weatherMapper::toForecastItemResponse)
                .toList();

    }

    private boolean isFresh(LocalDateTime lastUpdated) {
        return lastUpdated != null && lastUpdated.isAfter(LocalDateTime.now().minusHours(2));
    }

}
