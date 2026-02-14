package github.com.ifyosakwe.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import github.com.ifyosakwe.sky.models.entity.CurrentWeather;

import java.util.Optional;

public interface CurrentWeatherRepository extends JpaRepository<CurrentWeather, Long> {

    Optional<CurrentWeather> findByCityId(Long cityId);
}
