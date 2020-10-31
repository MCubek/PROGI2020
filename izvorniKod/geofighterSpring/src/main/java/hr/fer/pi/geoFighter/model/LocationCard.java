package hr.fer.pi.geoFighter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.spatial.Spatial;
import org.locationtech.jts.geom.Coordinates;
import org.locationtech.jts.geom.Point;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LOCATION_CARDS")
public class LocationCard {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "lcd_id")
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Lob
    private String description;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;

    @Column(name = "created_at")
    private final Instant createdDate = Instant.now();

    @NotNull
    private Point location;

    @NotNull
    private boolean accepted = false;

    @Column(name = "check_needed")
    private boolean needsToBeChecked;

    @Column(name = "enabled_date")
    private Instant enabledDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", referencedColumnName = "user_id")
    @NotNull
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accepted_by_user_id", referencedColumnName = "user_id")
    @NotNull
    private User acceptedBy;

    @OneToMany(mappedBy = "locationCard")
    private List<UserCard> userAssoc;

    @OneToMany(mappedBy = "locationCard")
    private List<UserCardFight> userFightAssoc;
}
