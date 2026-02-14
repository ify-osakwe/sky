package github.com.ifyosakwe.sky.models.dto.openweather;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeocodingResponse {
        @JsonProperty("name")
        private String name;

        @JsonProperty("lat")
        private double latitude;

        @JsonProperty("lon")
        private double longitude;

        @JsonProperty("country")
        private String country; // in ISO 3166 codes

        @JsonProperty("state")
        private String state;

        public GeocodingResponse() {
        }

        public GeocodingResponse(String name, double lat, double lon, String country) {
                this.name = name;
                this.latitude = lat;
                this.longitude = lon;
                this.country = country;
        }

        @Override
        public String toString() {
                return "GeocodingResponse [name=" + name + ", latitude=" + latitude + ", longitude=" + longitude
                                + ", country=" + country + ", state=" + state + "]";
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public double getLatitude() {
                return latitude;
        }

        public void setLatitude(double lat) {
                this.latitude = lat;
        }

        public double getLongitude() {
                return longitude;
        }

        public void setLongitude(double lon) {
                this.longitude = lon;
        }

        public String getCountry() {
                return country;
        }

        public void setCountry(String country) {
                this.country = country;
        }

        public String getState() {
                return state;
        }

        public void setState(String state) {
                this.state = state;
        }
}
