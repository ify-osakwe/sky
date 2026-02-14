package github.com.ifyosakwe.sky.models.dto.openweather;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ForecastResponse {

    public String cod;
    public int message;
    public int cnt;
    public List<ForecastItem> list;
    public City city;

    public static class ForecastItem {
        public long dt;
        public MainData main;
        public List<Weather> weather;
        public Clouds clouds;
        public Wind wind;
        public int visibility;
        public double pop;
        public Rain rain;
        public Sys sys;

        @JsonProperty("dt_txt")
        public String dtTxt;
    }

    public static class MainData {
        public double temp;

        @JsonProperty("feels_like")
        public double feelsLike;

        @JsonProperty("temp_min")
        public double tempMin;

        @JsonProperty("temp_max")
        public double tempMax;

        public int pressure;

        @JsonProperty("sea_level")
        public int seaLevel;

        @JsonProperty("grnd_level")
        public int grndLevel;

        public int humidity;

        @JsonProperty("temp_kf")
        public double tempKf;
    }

    public static class Weather {
        public int id;
        public String main;
        public String description;
        public String icon;
    }

    public static class Clouds {
        public int all;
    }

    public static class Wind {
        public double speed;
        public int deg;
        public double gust;
    }

    public static class Rain {
        @JsonProperty("3h")
        public double threeH;
    }

    public static class Sys {
        public String pod;
    }

    public static class City {
        public int id;
        public String name;
        public Coord coord;
        public String country;
        public int population;
        public int timezone;
        public long sunrise;
        public long sunset;
    }

    public static class Coord {
        public double lat;
        public double lon;
    }
}
