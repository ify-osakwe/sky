package github.com.ifyosakwe.sky.models.dto;

public class WeatherDto {
    private double longitude;
    private double latitude;
    private double tempKelvin;
    private double feelsLikeKelvin;
    private int pressure;
    private int humidity;
    private String weatherMain;
    private String weatherDescription;
    private double windSpeed;
    private int visibility;
    private int cloudsAll;
    private double rain;
    private double snow;
    private int timezone;
    private String cityName;
    private String cityCountry;

    public WeatherDto() {
    }

    public WeatherDto(double longitude, double latitude, double tempKelvin, double feelsLikeKelvin, int pressure,
            int humidity, String weatherMain, String weatherDescription, double windSpeed, int visibility,
            int cloudsAll, double rain, double snow, int timezone, String cityName, String cityCountry) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.tempKelvin = tempKelvin;
        this.feelsLikeKelvin = feelsLikeKelvin;
        this.pressure = pressure;
        this.humidity = humidity;
        this.weatherMain = weatherMain;
        this.weatherDescription = weatherDescription;
        this.windSpeed = windSpeed;
        this.visibility = visibility;
        this.cloudsAll = cloudsAll;
        this.rain = rain;
        this.snow = snow;
        this.timezone = timezone;
        this.cityName = cityName;
        this.cityCountry = cityCountry;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getTempKelvin() {
        return tempKelvin;
    }

    public void setTempKelvin(double tempKelvin) {
        this.tempKelvin = tempKelvin;
    }

    public double getFeelsLikeKelvin() {
        return feelsLikeKelvin;
    }

    public void setFeelsLikeKelvin(double feelsLikeKelvin) {
        this.feelsLikeKelvin = feelsLikeKelvin;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String description) {
        this.weatherDescription = description;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getCloudsAll() {
        return cloudsAll;
    }

    public void setCloudsAll(int cloudsAll) {
        this.cloudsAll = cloudsAll;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public double getSnow() {
        return snow;
    }

    public void setSnow(double snow) {
        this.snow = snow;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCountry() {
        return cityCountry;
    }

    public void setCityCountry(String cityCountry) {
        this.cityCountry = cityCountry;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    @Override
    public String toString() {
        return "WeatherDto [longitude=" + longitude + ", latitude=" + latitude + ", tempKelvin=" + tempKelvin
                + ", feelsLikeKelvin=" + feelsLikeKelvin + ", pressure=" + pressure + ", humidity=" + humidity
                + ", description=" + weatherDescription + ", windSpeed=" + windSpeed
                + ", visibility="
                + visibility + ", cloudsAll=" + cloudsAll + ", rain=" + rain + ", snow=" + snow + ", timezone="
                + timezone + ", cityName=" + cityName + ", cityCountry=" + cityCountry + "]";
    }
}
