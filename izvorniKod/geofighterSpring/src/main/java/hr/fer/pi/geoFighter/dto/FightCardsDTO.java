package hr.fer.pi.geoFighter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FightCardsDTO {
    List<Long> selectedCardIds;
}
