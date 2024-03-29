package dev.iagorodrigues.dscatalog.dto;

import dev.iagorodrigues.dscatalog.entities.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data @AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDTO implements Serializable {
    public static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "Campo nome é obrigatório")
    private String firstName;
    private String lastName;

    @Email(message = "Email inválido")
    private String email;

    @Setter(AccessLevel.NONE)
    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO(User entity) {
        id = entity.getId();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        email = entity.getEmail();
        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }
}
