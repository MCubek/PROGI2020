package hr.fer.pi.geoFighter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FightDTO {
    String username1;
    String username2;

    List<Long> user1selectedCardIds;
    List<Long> user2selectedCardIds;
}
