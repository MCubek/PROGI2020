package hr.fer.pi.geoFighter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;

    @Column(name = "cartographer_Confirmed")
    private boolean cartographerConfirmed;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] idCardPhoto;

    @Nullable
    private String iban;

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

    @OneToMany(mappedBy = "user")
    private List<UserCard> locationCardAssoc;

    @OneToMany(mappedBy = "createdBy")
    private Set<LocationCard> createdCards;

    @OneToMany(mappedBy = "acceptedBy")
    private Set<LocationCard> acceptedCards;

    @OneToMany(mappedBy = "user")
    private List<UserCardFight> cardFightAssoc;

    @OneToMany(mappedBy = "winner")
    private Set<Fight> fightsWon;
}
