package github.com.ifyosakwe.sky.models.dto.openweather;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class WeatherResponse {
    public Coord coord;
    public List<Weather> weather;
    public String base;
    public Main main;
    public int visibility;
    public Wind wind;
    public Rain rain;
    public Snow snow;
    public Clouds clouds;
    public long dt;
    public Sys sys;
    public int timezone;
    public int id;
    public String name;
    public int cod;

    public static class Coord {
        @JsonProperty("lon")
        public double longitude;
        @JsonProperty("lat")
        public double latitude;
    }

    public static class Weather {
        public int id;
        public String main;
        public String description;
        public String icon;
    }

    public static class Main {
        public double temp;
        @JsonProperty("feels_like")
        public double feelsLike;
        @JsonProperty("temp_min")
        public double tempMin;
        @JsonProperty("temp_max")
        public double tempMax;
        public int pressure;
        public int humidity;
        @JsonProperty("sea_level")
        public int seaLevel;
        @JsonProperty("grnd_level")
        public int groundLevel;
    }

    public static class Wind {
        public double speed;
        @JsonProperty("deg")
        public int direction;
        public double gust;
    }

    public static class Rain {
        @JsonProperty("1h")
        public double oneHour;
    }

    public static class Snow {
        @JsonProperty("1h")
        public double oneHour;
    }

    public static class Clouds {
        public int all;
    }

    public static class Sys {
        public int type;
        public int id;
        public String country;
        public long sunrise;
        public long sunset;
    }
}
