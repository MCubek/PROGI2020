package hr.fer.pi.geoFighter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserCardDTO {
    private String name;
    private String description;
    private URL photoURL;
}
