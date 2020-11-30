package hr.fer.pi.geoFighter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_card")
@IdClass(UserCardId.class)
public class UserCard {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "lcd_id", referencedColumnName = "lcd_id")
    private LocationCard locationCard;

    @Column(name = "cooldown_multiplier")
    @Nullable
    private Double cooldownMultiplier;

    @Column(name = "cooldown_end")
    @Nullable
    private LocalDateTime cooldownEndTime;

}

