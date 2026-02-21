package github.com.ifyosakwe.sky.service;

import github.com.ifyosakwe.sky.exception.CityNotFoundException;
import github.com.ifyosakwe.sky.models.dto.CityDto;
import github.com.ifyosakwe.sky.models.dto.openweather.ForecastResponse;
import github.com.ifyosakwe.sky.models.dto.openweather.GeocodingResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// TODO: Add a Scheduler to call refreshWeatherData()
// deletes old forecasts, fetches & saves fresh ones
// to keep data current.
// then if (!forcastListDB.isEmpty()) {...} simplified

@Service
public class WeatherService {

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
        if (cityDB.isPresent() && isFresh(cityDB.get().getCurrentWeather().getLastUpdated())) {
            cityService.incrementSearchCount(cityDB.get().getId());
            return weatherMapper.toCurrentWeatherResponse(cityDB.get());
        }

        City city = cityService.findOrCreateCity(cityDto);
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

    @Transactional
    public List<ForecastItemResponse> getForecast(CityDto cityDto) {
        Optional<City> cityDB = cityService.findByNameAndCountry(
                cityDto.getName(), cityDto.getCountry());
        if (cityDB.isPresent()) {
            List<Forecast> forcastListDB = forecastRepository.findByCityIdAndForecastDateAfter(
                    cityDB.get().getId(), LocalDateTime.now());
            if (!forcastListDB.isEmpty() && isFresh(forcastListDB.get(0).getForecastDate())) {
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

        return forecastList.stream()
                .map(weatherMapper::toForecastItemResponse)
                .toList();

    }

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

    private boolean isFresh(LocalDateTime lastUpdated) {
        return lastUpdated != null && lastUpdated.isAfter(LocalDateTime.now().minusHours(2));
    }

}
