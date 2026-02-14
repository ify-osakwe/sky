package github.com.ifyosakwe.sky.models.dto.response;

import java.time.LocalDateTime;

public class CurrentWeatherResponse {
    private String cityName;
    private String country;
    private Double temperature;
    private Integer humidity;
    private Integer pressure;
    private Double windSpeed;
    private Integer windDirection;
    private String weatherMain;
    private String weatherDescription;
    private LocalDateTime sunrise;
    private LocalDateTime sunset;
    private LocalDateTime lastUpdated;

    public CurrentWeatherResponse() {
    }

    public CurrentWeatherResponse(String cityName, String country, Double temperature,
            Integer humidity, Integer pressure, Double windSpeed,
            Integer windDirection, String weatherMain,
            String weatherDescription, LocalDateTime sunrise,
            LocalDateTime sunset, LocalDateTime lastUpdated) {
        this.cityName = cityName;
        this.country = country;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.weatherMain = weatherMain;
        this.weatherDescription = weatherDescription;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.lastUpdated = lastUpdated;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(Integer windDirection) {
        this.windDirection = windDirection;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public LocalDateTime getSunrise() {
        return sunrise;
    }

    public void setSunrise(LocalDateTime sunrise) {
        this.sunrise = sunrise;
    }

    public LocalDateTime getSunset() {
        return sunset;
    }

    public void setSunset(LocalDateTime sunset) {
        this.sunset = sunset;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
