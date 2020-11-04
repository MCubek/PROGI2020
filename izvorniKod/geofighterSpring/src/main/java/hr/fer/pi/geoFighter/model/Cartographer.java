package hr.fer.pi.geoFighter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
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
@Entity(name = "cartographer")
public class Cartographer extends User {
    @Column(name = "cartographer_Confirmed")
    private boolean cartographerConfirmed;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] idCardPhoto;

    @Nullable
    private String iban;

    @OneToMany(mappedBy = "acceptedBy", fetch = FetchType.LAZY)
    private Set<LocationCard> acceptedCards;

    @ManyToOne
    private Administrator confirmedBy;
}
