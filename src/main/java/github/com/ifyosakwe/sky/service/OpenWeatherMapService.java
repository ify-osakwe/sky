package github.com.ifyosakwe.sky.service;

import java.util.List;

import github.com.ifyosakwe.sky.models.dto.openweather.ForecastResponse;
import github.com.ifyosakwe.sky.models.dto.openweather.GeocodingResponse;
import github.com.ifyosakwe.sky.models.dto.openweather.WeatherResponse;
import github.com.ifyosakwe.sky.models.dto.openweather.ForecastResponse.ForecastItem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OpenWeatherMapService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.base-url}")
    private String baseUrl;

    @Value("${openweathermap.api.geo-url}")
    private String geoUrl;

    private final RestClient restClient;

    public OpenWeatherMapService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public List<GeocodingResponse> searchCities(String query) {
        return restClient.get()
                .uri(geoUrl + "?q={query}&limit=5&appid={key}", query, apiKey)
                .retrieve()
                .body(new ParameterizedTypeReference<List<GeocodingResponse>>() {
                });
    }

    public WeatherResponse fetchCurrentWeather(double lat, double lon) {
        String url = baseUrl + "/weather?lat={lat}&lon={lon}&appid={key}";
        return restClient.get()
                .uri(url, lat, lon, apiKey)
                .retrieve()
                .body(WeatherResponse.class);
    }

    public ForecastResponse fetchForecast(double lat, double lon) {
        String url = baseUrl + "/forecast?lat={lat}&lon={lon}&appid={key}";
        return restClient.get()
                .uri(url, lat, lon, apiKey)
                .retrieve()
                .body(ForecastResponse.class);
    }

    public List<ForecastItem> fetchDailyForecast(double lat, double lon) {
        ForecastResponse fullForecast = fetchForecast(lat, lon);

        // Filter to only show forecasts for 12:00 PM
        return fullForecast.list.stream()
                .filter(item -> item.dtTxt.contains("12:00:00"))
                .toList();
    }

}
