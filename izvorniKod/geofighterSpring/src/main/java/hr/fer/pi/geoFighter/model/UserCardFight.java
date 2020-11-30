package hr.fer.pi.geoFighter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_card_fight")
@IdClass(UserCardFightId.class)
public class UserCardFight {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "lcd_id", referencedColumnName = "lcd_id")
    private LocationCard locationCard;

    @Id
    @ManyToOne
    @JoinColumn(name = "fight_id", referencedColumnName = "fight_id")
    private Fight fight;
}
