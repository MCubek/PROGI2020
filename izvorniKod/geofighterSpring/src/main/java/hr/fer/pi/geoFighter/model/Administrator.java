package hr.fer.pi.geoFighter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * @author MatejCubek
 * @project pi
 * @created 04/11/2020
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "administrator")
public class Administrator extends User {
    @OneToMany(mappedBy = "confirmedBy")
    Set<Cartographer> approvedCartographers;
}
