package dev.iagorodrigues.dscatalog.dto;

import dev.iagorodrigues.dscatalog.services.validation.UserInsertValid;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
@UserInsertValid
public class UserInsertDTO extends UserDTO {
    public static final long serialVersionUID = 1L;

    @Size(min = 8, message = "Senha deve possuir, no mínimo, 8 caracteres")
    @NotBlank(message = "Campo senha é obrigatório")
    private String password;

    public UserInsertDTO() {
        super();
    }

}