package github.com.ifyosakwe.sky.service;

import github.com.ifyosakwe.sky.models.dto.skyapi.CityDto;
import github.com.ifyosakwe.sky.models.entity.City;
import github.com.ifyosakwe.sky.repository.CityRepository;
import github.com.ifyosakwe.sky.repository.CurrentWeatherRepository;
import github.com.ifyosakwe.sky.repository.ForecastRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeatherRefreshService {

    private static final Logger log = LoggerFactory.getLogger(WeatherRefreshService.class);

    private final CityRepository cityRepository;
    private final WeatherService weatherService;
    private final CurrentWeatherRepository currentWeatherRepository;
    private final ForecastRepository forecastRepository;

    @Value("${sky.scheduler.refresh-city-count:10}")
    private int refreshCityCount;

    @Value("${sky.scheduler.stale-data-days:30}")
    private int staleDataDays;

    public WeatherRefreshService(CityRepository cityRepository,
            WeatherService weatherService,
            CurrentWeatherRepository currentWeatherRepository,
            ForecastRepository forecastRepository) {
        this.cityRepository = cityRepository;
        this.weatherService = weatherService;
        this.currentWeatherRepository = currentWeatherRepository;
        this.forecastRepository = forecastRepository;
    }

    @Scheduled(fixedRateString = "${sky.scheduler.refresh-interval-ms:1800000}")
    public void refreshFrequentlySearchedCities() {
        log.info("Starting scheduled weather refresh for top {} cities", refreshCityCount);

        PageRequest pageRequest = PageRequest.of(
                0, refreshCityCount, Sort.by(Sort.Direction.DESC, "searchCount"));
        List<City> topCities = cityRepository.findByLastSearchedIsNotNull(pageRequest);

        if (topCities.isEmpty()) {
            log.info("No frequently searched cities found. Skipping refresh.");
            return;
        }

        int successCount = 0;
        int failCount = 0;

        for (City city : topCities) {
            try {
                CityDto cityDto = new CityDto(
                        city.getName(),
                        city.getCountry(),
                        city.getLatitude().doubleValue(),
                        city.getLongitude().doubleValue());

                weatherService.refreshCurrentWeather(cityDto);
                weatherService.refreshForecast(cityDto);
                successCount++;
                log.debug("Refreshed weather for city: {}, {}", city.getName(), city.getCountry());
            } catch (Exception e) {
                failCount++;
                log.error("Failed to refresh weather for city: {}, {} — {}",
                        city.getName(), city.getCountry(), e.getMessage());
            }
        }

        log.info("Weather refresh completed. Success: {}, Failed: {}", successCount, failCount);
    }

    @Scheduled(cron = "${sky.scheduler.cleanup-cron:0 0 3 * * ?}")
    @Transactional
    public void cleanupStaleData() {
        log.info("Starting stale data cleanup (threshold: {} days)", staleDataDays);

        LocalDateTime cutoff = LocalDateTime.now().minusDays(staleDataDays);
        List<City> staleCities = cityRepository.findByLastSearchedBeforeOrLastSearchedIsNull(cutoff);

        if (staleCities.isEmpty()) {
            log.info("No stale cities found. Skipping cleanup.");
            return;
        }

        int cleanedCount = 0;
        for (City city : staleCities) {
            try {
                currentWeatherRepository.deleteByCityId(city.getId());
                forecastRepository.deleteByCityId(city.getId());
                cityRepository.delete(city);
                cleanedCount++;
                log.debug("Cleaned up stale city: {}, {}", city.getName(), city.getCountry());
            } catch (Exception e) {
                log.error("Failed to clean up city: {}, {} — {}",
                        city.getName(), city.getCountry(), e.getMessage());
            }
        }

        log.info("Stale data cleanup completed. Removed {} cities and their weather data.", cleanedCount);
    }
}
