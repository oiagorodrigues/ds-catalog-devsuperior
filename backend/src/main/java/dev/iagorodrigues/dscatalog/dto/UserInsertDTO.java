package dev.iagorodrigues.dscatalog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserInsertDTO extends UserDTO {

    private String password;

    public UserInsertDTO() {
        super();
    }

}
