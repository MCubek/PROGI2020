package hr.fer.pi.geoFighter;

import hr.fer.pi.geoFighter.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class GeoFighterApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoFighterApplication.class, args);
	}

}
