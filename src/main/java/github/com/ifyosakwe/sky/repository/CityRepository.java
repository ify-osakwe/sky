package github.com.ifyosakwe.sky.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import github.com.ifyosakwe.sky.models.entity.City;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    /**
     * Find a city by name and country code.
     * SELECT * FROM cities
     * WHERE name = :name AND country = :country
     * LIMIT 1;
     */
    Optional<City> findByNameAndCountry(String name, String country);

    /**
     * Find cities that have been searched, ordered by the pageable settings.
     * SELECT * FROM cities
     * WHERE last_searched IS NOT NULL
     * ORDER BY search_count DESC
     * LIMIT 10 OFFSET 0;
     */
    List<City> findByLastSearchedIsNotNull(Pageable pageable);

    /**
     * Find cities whose name contains the search term (for autocomplete).
     * SELECT * FROM cities
     * WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'));
     */
    List<City> findByNameContainingIgnoreCase(String name);
}
