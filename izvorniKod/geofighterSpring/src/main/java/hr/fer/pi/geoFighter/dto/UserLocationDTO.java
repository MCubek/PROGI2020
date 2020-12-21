package hr.fer.pi.geoFighter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationDTO {
    private Double longitude;
    private Double latitude;
}