package github.com.ifyosakwe.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import github.com.ifyosakwe.sky.models.entity.Forecast;

import java.time.LocalDateTime;
import java.util.List;

public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    List<Forecast> findByCityIdAndForecastDateAfter(Long cityId, LocalDateTime after);

    void deleteByCityId(Long cityId);
}
