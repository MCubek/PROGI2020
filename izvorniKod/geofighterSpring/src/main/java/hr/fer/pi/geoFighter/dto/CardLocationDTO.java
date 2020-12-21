package hr.fer.pi.geoFighter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MatejCubek
 * @project pi
 * @created 21/12/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardLocationDTO {
    Long cardId;
    Double latitude;
    Double longitude;
}
