package github.com.ifyosakwe.sky.models.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class RecentCitiesResponse {
    private List<RecentCity> cities;

    public RecentCitiesResponse() {
    }

    public RecentCitiesResponse(List<RecentCity> cities) {
        this.cities = cities;
    }

    public List<RecentCity> getCities() {
        return cities;
    }

    public void setCities(List<RecentCity> cities) {
        this.cities = cities;
    }

    public static class RecentCity {
        private String name;
        private String country;
        private Integer searchCount;
        private LocalDateTime lastSearched;

        public RecentCity() {
        }

        public RecentCity(String name, String country, Integer searchCount, LocalDateTime lastSearched) {
            this.name = name;
            this.country = country;
            this.searchCount = searchCount;
            this.lastSearched = lastSearched;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public Integer getSearchCount() {
            return searchCount;
        }

        public void setSearchCount(Integer searchCount) {
            this.searchCount = searchCount;
        }

        public LocalDateTime getLastSearched() {
            return lastSearched;
        }

        public void setLastSearched(LocalDateTime lastSearched) {
            this.lastSearched = lastSearched;
        }
    }
}
