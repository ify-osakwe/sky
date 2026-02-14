package github.com.ifyosakwe.sky.models.dto.response;

import java.time.LocalDateTime;

public class ForecastItemResponse {
    private LocalDateTime forecastDate;
    private Double temperature;
    private Integer humidity;
    private Integer pressure;
    private Double windSpeed;
    private String weatherMain;
    private String weatherDescription;
    private Double rainVolume;
    private Double precipitationProbability;

    public ForecastItemResponse() {
    }

    public ForecastItemResponse(LocalDateTime forecastDate, Double temperature,
            Integer humidity, Integer pressure, Double windSpeed,
            String weatherMain, String weatherDescription,
            Double rainVolume, Double precipitationProbability) {
        this.forecastDate = forecastDate;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.weatherMain = weatherMain;
        this.weatherDescription = weatherDescription;
        this.rainVolume = rainVolume;
        this.precipitationProbability = precipitationProbability;
    }

    public LocalDateTime getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(LocalDateTime forecastDate) {
        this.forecastDate = forecastDate;
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

    public Double getRainVolume() {
        return rainVolume;
    }

    public void setRainVolume(Double rainVolume) {
        this.rainVolume = rainVolume;
    }

    public Double getPrecipitationProbability() {
        return precipitationProbability;
    }

    public void setPrecipitationProbability(Double precipitationProbability) {
        this.precipitationProbability = precipitationProbability;
    }
}
