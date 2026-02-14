package github.com.ifyosakwe.sky.models.dto;

public class ForecastDto {
    private double tempKelvin;
    private double feelTempkelvin;
    private int pressure;
    private int humidity;
    private int visibility;

    public ForecastDto() {
    }

    public ForecastDto(double tempKelvin, double feelTempkelvin, int pressure, int humidity, int visibility) {
        this.tempKelvin = tempKelvin;
        this.feelTempkelvin = feelTempkelvin;
        this.pressure = pressure;
        this.humidity = humidity;
        this.visibility = visibility;
    }

    public double getTempKelvin() {
        return tempKelvin;
    }

    public void setTempKelvin(double tempKelvin) {
        this.tempKelvin = tempKelvin;
    }

    public double getFeelTempkelvin() {
        return feelTempkelvin;
    }

    public void setFeelTempkelvin(double feelTempkelvin) {
        this.feelTempkelvin = feelTempkelvin;
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

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

}
