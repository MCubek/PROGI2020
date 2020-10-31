package hr.fer.pi.geoFighter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GeoFighterApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoFighterApplication.class, args);
	}

}
