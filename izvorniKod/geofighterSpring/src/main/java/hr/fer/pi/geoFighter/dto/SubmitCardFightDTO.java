package hr.fer.pi.geoFighter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MatejCubek
 * @project pi
 * @created 06/01/2021
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitCardFightDTO {
    Long cardId;
}
