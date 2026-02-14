package github.com.ifyosakwe.sky;

import org.springframework.boot.SpringApplication;

public class TestSkyApplication {

	public static void main(String[] args) {
		SpringApplication.from(SkyApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
