package github.com.ifyosakwe.sky.models.dto.skyapi;

public class CityDto {
    private String name;
    private String state;
    private String country;
    private double latitude;
    private double longitude;

    public CityDto() {
    }

    public CityDto(String name, String country, double latitude,
            double longitude) {
        this.name = name;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CityDto(String name, String state, String country, double latitude,
            double longitude) {
        this.name = name;
        this.state = state;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "CityDto [name=" + name + ", state=" + state
                + ", country=" + country + ", latitude=" + latitude + ", longitude=" + longitude + "]";
    }
}
