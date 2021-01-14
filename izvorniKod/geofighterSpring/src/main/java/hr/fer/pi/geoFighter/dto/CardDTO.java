package hr.fer.pi.geoFighter.dto;

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
    private URL photoURL;
    private String location;
    private Double longitude;
    private Double latitude;
    private String createdBy;
    private Integer uncommonness;
    private Integer difficulty;
    private Integer population;
    private String createdTime;
}
