package hr.fer.pi.geoFighter.dto;

import hr.fer.pi.geoFighter.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDTO {

    private Long id;
    private String name;
    private String description;
    private URL photoUrl;
    private String location;
    private User createdBy;
    private Integer uncommonness;
    private Integer difficulty;
    private Integer population;
}
