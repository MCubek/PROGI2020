package hr.fer.pi.geoFighter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisableUserDTO {
    String username;
    String timeoutEnd;
}
