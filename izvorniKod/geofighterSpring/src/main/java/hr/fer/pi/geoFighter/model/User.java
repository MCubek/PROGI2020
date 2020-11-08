package hr.fer.pi.geoFighter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "player")
@Table(name = "\"USERS\"", uniqueConstraints = {@UniqueConstraint(columnNames = "username"), @UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "Username is required")
    private String username;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private final Instant createdTime = Instant.now();

    @NotNull
    private boolean enabled = false;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;

    @NotNull
    private Integer eloScore = 0;

    @NotNull
    private Integer wins = 0;

    @NotNull
    private Integer losses = 0;

    @Nullable
    private LocalDateTime forcedTimeoutEnd = null;

    @Nullable
    private Point currentLocation;

    private Boolean online;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<UserCard> locationCardAssoc;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private Collection<LocationCard> createdCards;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<UserCardFight> cardFightAssoc;

    @OneToMany(mappedBy = "winner", fetch = FetchType.LAZY)
    private Collection<Fight> fightsWon;

    @OneToOne(mappedBy = "user", orphanRemoval = true)
    private VerificationToken token;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;

    @Column(name = "cartographer_status")
    private CartographerStatus cartographerStatus = CartographerStatus.NOT_REQUESTED;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] idCardPhoto;

    @Nullable
    private String iban;

    @OneToMany(mappedBy = "acceptedBy", fetch = FetchType.LAZY)
    private Set<LocationCard> acceptedCards;
}
