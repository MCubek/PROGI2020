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
public class CardApplicationDTO {
    private Long id;
    private String name;
    private String description;
    private URL photoUrl;
    private String location;
    private String createdBy;
    private Boolean needsToBeChecked;
    private Integer uncommonness;
    private Integer difficulty;
    private Integer population;
    private String createdTime;
}
