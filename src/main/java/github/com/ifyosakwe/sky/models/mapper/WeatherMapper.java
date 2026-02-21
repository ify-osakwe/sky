package github.com.ifyosakwe.sky.models.mapper;

import github.com.ifyosakwe.sky.models.dto.openweather.ForecastResponse;
import github.com.ifyosakwe.sky.models.dto.openweather.WeatherResponse;
import github.com.ifyosakwe.sky.models.dto.openweather.ForecastResponse.ForecastItem;
import github.com.ifyosakwe.sky.models.dto.response.CurrentWeatherApiResponse;
import github.com.ifyosakwe.sky.models.dto.response.ForecastItemResponse;
import github.com.ifyosakwe.sky.models.entity.City;
import github.com.ifyosakwe.sky.models.entity.CurrentWeather;
import github.com.ifyosakwe.sky.models.entity.Forecast;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherMapper {

    public CurrentWeather toCurrentWeather(WeatherResponse dto, City city) {
        CurrentWeather weather = new CurrentWeather();
        weather.setCity(city);
        weather.setTimestamp(LocalDateTime.ofInstant(Instant.ofEpochSecond(dto.dt), ZoneOffset.UTC));
        weather.setTemperature(dto.main.temp);
        weather.setHumidity(dto.main.humidity);
        weather.setPressure(dto.main.pressure);

        if (dto.wind != null) {
            weather.setWindSpeed(dto.wind.speed);
            weather.setWindDirection(dto.wind.direction);
        }

        if (dto.weather != null && !dto.weather.isEmpty()) {
            weather.setWeatherMain(dto.weather.get(0).main);
            weather.setWeatherDescription(dto.weather.get(0).description);
        }

        if (dto.sys != null) {
            weather.setSunrise(LocalDateTime.ofInstant(Instant.ofEpochSecond(dto.sys.sunrise), ZoneOffset.UTC));
            weather.setSunset(LocalDateTime.ofInstant(Instant.ofEpochSecond(dto.sys.sunset), ZoneOffset.UTC));
        }

        weather.setLastUpdated(LocalDateTime.now());

        return weather;
    }

    /*
     * public CurrentWeather toCurrentWeather(WeatherResponse weatherResponse) {
     * CurrentWeather weather = new CurrentWeather();
     * weather.setCity(weatherResponse.city);
     * weather.setTimestamp(LocalDateTime.ofInstant(Instant.ofEpochSecond(
     * weatherResponse.dt), ZoneOffset.UTC));
     * weather.setTemperature(weatherResponse.main.temp);
     * weather.setHumidity(weatherResponse.main.humidity);
     * weather.setPressure(weatherResponse.main.pressure);
     * 
     * if (weatherResponse.wind != null) {
     * weather.setWindSpeed(weatherResponse.wind.speed);
     * weather.setWindDirection(weatherResponse.wind.direction);
     * }
     * 
     * if (weatherResponse.weather != null && !weatherResponse.weather.isEmpty()) {
     * weather.setWeatherMain(weatherResponse.weather.get(0).main);
     * weather.setWeatherDescription(weatherResponse.weather.get(0).description);
     * }
     * 
     * if (weatherResponse.sys != null) {
     * weather.setSunrise(
     * LocalDateTime.ofInstant(Instant.ofEpochSecond(weatherResponse.sys.sunrise),
     * ZoneOffset.UTC));
     * weather.setSunset(
     * LocalDateTime.ofInstant(Instant.ofEpochSecond(weatherResponse.sys.sunset),
     * ZoneOffset.UTC));
     * }
     * 
     * weather.setLastUpdated(LocalDateTime.now());
     * 
     * return weather;
     * }
     */


    public List<Forecast> toForecasts(List<ForecastItem> dtoList, City city) {
        List<Forecast> forecasts = new ArrayList<>();
        if (dtoList == null) {
            return forecasts;
        }

        for (ForecastItem item : dtoList) {
            Forecast forecast = new Forecast();
            forecast.setCity(city);
            forecast.setForecastDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(item.dt), ZoneOffset.UTC));
            forecast.setTemperature(item.main.temp);
            forecast.setHumidity(item.main.humidity);
            forecast.setPressure(item.main.pressure);

            if (item.wind != null) {
                forecast.setWindSpeed(item.wind.speed);
                forecast.setWindDirection(item.wind.deg);
            }

            if (item.weather != null && !item.weather.isEmpty()) {
                forecast.setWeatherMain(item.weather.get(0).main);
                forecast.setWeatherDescription(item.weather.get(0).description);
            }

            if (item.rain != null) {
                forecast.setRainVolume(item.rain.threeH);
            }

            forecast.setProbability(item.pop);
            forecasts.add(forecast);
        }

        return forecasts;
    }

    public List<Forecast> toForecasts(ForecastResponse dto, City city) {
        List<Forecast> forecasts = new ArrayList<>();

        if (dto.list == null) {
            return forecasts;
        }

        for (ForecastResponse.ForecastItem item : dto.list) {
            Forecast forecast = new Forecast();
            forecast.setCity(city);
            forecast.setForecastDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(item.dt), ZoneOffset.UTC));
            forecast.setTemperature(item.main.temp);
            forecast.setHumidity(item.main.humidity);
            forecast.setPressure(item.main.pressure);

            if (item.wind != null) {
                forecast.setWindSpeed(item.wind.speed);
                forecast.setWindDirection(item.wind.deg);
            }

            if (item.weather != null && !item.weather.isEmpty()) {
                forecast.setWeatherMain(item.weather.get(0).main);
                forecast.setWeatherDescription(item.weather.get(0).description);
            }

            if (item.rain != null) {
                forecast.setRainVolume(item.rain.threeH);
            }

            forecast.setProbability(item.pop);

            forecasts.add(forecast);
        }

        return forecasts;
    }

    public List<Forecast> toForecasts(List<ForecastItem> dtoList) {
        List<Forecast> forecasts = new ArrayList<>();
        for (ForecastItem item : dtoList) {
            Forecast forecast = new Forecast();
            forecast.setForecastDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(item.dt), ZoneOffset.UTC));
            forecast.setTemperature(item.main.temp);
            forecast.setHumidity(item.main.humidity);
            forecast.setPressure(item.main.pressure);

            if (item.wind != null) {
                forecast.setWindSpeed(item.wind.speed);
                forecast.setWindDirection(item.wind.deg);
            }

            if (item.weather != null && !item.weather.isEmpty()) {
                forecast.setWeatherMain(item.weather.get(0).main);
                forecast.setWeatherDescription(item.weather.get(0).description);
            }

            if (item.rain != null) {
                forecast.setRainVolume(item.rain.threeH);
            }

            forecast.setProbability(item.pop);

            forecasts.add(forecast);
        }
        return forecasts;
    }

    public CurrentWeatherApiResponse toCurrentWeatherResponse(CurrentWeather weather, City city) {
        return new CurrentWeatherApiResponse(
                city.getName(),
                city.getCountry(),
                weather.getTemperature(),
                weather.getHumidity(),
                weather.getPressure(),
                weather.getWindSpeed(),
                weather.getWindDirection(),
                weather.getWeatherMain(),
                weather.getWeatherDescription(),
                weather.getSunrise(),
                weather.getSunset(),
                weather.getLastUpdated());
    }

    public CurrentWeatherApiResponse toCurrentWeatherResponse(City city) {
        return new CurrentWeatherApiResponse(
                city.getName(),
                city.getCountry(),
                city.getCurrentWeather().getTemperature(),
                city.getCurrentWeather().getHumidity(),
                city.getCurrentWeather().getPressure(),
                city.getCurrentWeather().getWindSpeed(),
                city.getCurrentWeather().getWindDirection(),
                city.getCurrentWeather().getWeatherMain(),
                city.getCurrentWeather().getWeatherDescription(),
                city.getCurrentWeather().getSunrise(),
                city.getCurrentWeather().getSunset(),
                city.getCurrentWeather().getLastUpdated());
    }

    public ForecastItemResponse toForecastItemResponse(Forecast forecast) {
        return new ForecastItemResponse(
                forecast.getForecastDate(),
                forecast.getTemperature(),
                forecast.getHumidity(),
                forecast.getPressure(),
                forecast.getWindSpeed(),
                forecast.getWeatherMain(),
                forecast.getWeatherDescription(),
                forecast.getRainVolume(),
                forecast.getProbability());
    }
}
