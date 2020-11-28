package hr.fer.pi.geoFighter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.Date;

/**
 * @author MatejCubek
 * @project pi
 * @created 28/11/2020
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartographerUserDTO {
    private String username;
    private String email;
    private Date joinedDate;
    private URL photoUrl;
    private Integer eloScore;
    private URL idUrl;
}
