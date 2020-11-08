package hr.fer.pi.geoFighter.repository;

import hr.fer.pi.geoFighter.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
