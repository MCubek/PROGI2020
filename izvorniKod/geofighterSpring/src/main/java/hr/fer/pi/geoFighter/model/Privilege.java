package hr.fer.pi.geoFighter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

/**
 * @author MatejCubek
 * @project pi
 * @created 05/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Privileges")
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long privilegeId;

    @NotBlank
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;
}
