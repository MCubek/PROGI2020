package hr.fer.pi.geoFighter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SendRequestDTO {
    String usernameReceiver;
    String usernameSender;
    boolean answer;
    Long battleId;
}
