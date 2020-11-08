package hr.fer.pi.geoFighter.repository;

import hr.fer.pi.geoFighter.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> getImageById(Long id);
}
