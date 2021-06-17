package dev.iagorodrigues.dscatalog.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class UserInsertDTO extends UserDTO {

    @Size(min = 8, message = "Senha deve possuir, no mínimo, 8 caracteres")
    @NotBlank(message = "Campo senha é obrigatório")
    private String password;

    public UserInsertDTO() {
        super();
    }

}
