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

public class UserCardDTO {
    private Long id;
    private String name;
    private Integer difficulty;
    private Integer population;
    private Integer uncommonness;
    private URL photoURL;
}
