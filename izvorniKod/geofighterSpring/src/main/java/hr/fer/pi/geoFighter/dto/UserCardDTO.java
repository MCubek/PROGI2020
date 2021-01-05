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
    private String difficulty;
    private String population;
    private String uncommonness;
    private String description;
    private URL photoURL;
}
