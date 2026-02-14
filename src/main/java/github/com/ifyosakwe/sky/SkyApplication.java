package github.com.ifyosakwe.sky;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import github.com.ifyosakwe.sky.service.OpenWeatherMapService;

@SpringBootApplication
public class SkyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkyApplication.class, args);
	}

	/*@Bean
	CommandLineRunner testApi(OpenWeatherMapService service) {
		return args -> {
			System.out.println("=== Testing OpenWeatherMap API ===");
			var cities = service.searchCities("London");
			System.out.println("Found these [London] cities: " + cities);
			System.out.println("Found: " + cities.get(0).getName());

			var weather = service.fetchCurrentWeather(
					cities.get(0).getLatitude(),
					cities.get(0).getLongitude());
			System.out.println("Temp: " + weather.main.temp + "K");
			System.out.println("=== API Test Complete ===");
		};
	}*/

}
