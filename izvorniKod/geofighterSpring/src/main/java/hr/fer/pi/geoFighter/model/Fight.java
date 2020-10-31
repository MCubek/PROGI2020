package hr.fer.pi.geoFighter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FIGHTS")
public class Fight {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "fight_id")
    private Long fightId;

    @Column(name = "start_time")
    private LocalDateTime startTime = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_user_id", referencedColumnName = "user_id")
    private User winner;
}
