package hr.fer.pi.geoFighter.repository;

import hr.fer.pi.geoFighter.model.LocationCard;
import hr.fer.pi.geoFighter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationCardRepository extends JpaRepository<LocationCard, Long> {
    Optional<LocationCard> getLocationCardById(Long id);

    List<LocationCard> getLocationCardsByName(String name);

    List<LocationCard> getLocationCardsByCreatedBy(User createdBy);

    List<LocationCard> getLocationCardsByAcceptedBy(User acceptedBy);
}
