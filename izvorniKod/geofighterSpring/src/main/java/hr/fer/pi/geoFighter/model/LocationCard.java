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
    @Size(max = 4000)
    private String description;

    @Lob
    private byte[] photo;

    @Column(name = "created_at")
    private final LocalDateTime createdDate = LocalDateTime.now();

    @NotNull
    private Point location;

    @NotNull
    private boolean accepted = false;

    @Column(name = "check_needed")
    private boolean needsToBeChecked;

    @Column(name = "enabled_date")
    private LocalDateTime enabledDate;

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
