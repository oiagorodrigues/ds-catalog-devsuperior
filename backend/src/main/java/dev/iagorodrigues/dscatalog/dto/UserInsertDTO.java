package dev.iagorodrigues.dscatalog.dto;

import dev.iagorodrigues.dscatalog.services.validation.user.UserInsertValid;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@UserInsertValid
public class UserInsertDTO extends UserDTO {
    public static final long serialVersionUID = 1L;

    @Length(min = 8, message = "Senha deve possuir no mínimo {min} caracteres")
    @NotBlank(message = "Campo senha é obrigatório")
    private String password;

    public UserInsertDTO() {
        super();
    }

}
