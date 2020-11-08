package hr.fer.pi.geoFighter.dto;

import hr.fer.pi.geoFighter.model.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

/**
 * @author MatejCubek
 * @project pi
 * @created 07/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartographerRegisterRequest {
    @NotBlank
    private String iban;
    private MultipartFile idPhoto;
}
