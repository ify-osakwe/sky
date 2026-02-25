package github.com.ifyosakwe.sky.config;

import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${sky.cache.current-weather-ttl-minutes:15}")
    private long currentWeatherTtlMinutes;

    @Value("${sky.cache.forecast-ttl-minutes:60}")
    private long forecastTtlMinutes;

    @Value("${sky.cache.city-search-ttl-hours:24}")
    private long citySearchTtlHours;

    @Value("${sky.cache.max-size:100}")
    private long maxSize;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        CaffeineCache currentWeatherCache = new CaffeineCache("currentWeather",
                Caffeine.newBuilder()
                        .expireAfterWrite(currentWeatherTtlMinutes, TimeUnit.MINUTES)
                        .maximumSize(maxSize)
                        .build());

        CaffeineCache forecastCache = new CaffeineCache("forecast",
                Caffeine.newBuilder()
                        .expireAfterWrite(forecastTtlMinutes, TimeUnit.MINUTES)
                        .maximumSize(maxSize)
                        .build());

        CaffeineCache citySearchCache = new CaffeineCache("citySearch",
                Caffeine.newBuilder()
                        .expireAfterWrite(citySearchTtlHours, TimeUnit.HOURS)
                        .maximumSize(200)
                        .build());

        cacheManager.setCaches(List.of(currentWeatherCache, forecastCache, citySearchCache));
        return cacheManager;
    }
}
