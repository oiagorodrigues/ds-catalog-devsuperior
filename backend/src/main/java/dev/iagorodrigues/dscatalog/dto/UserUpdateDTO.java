package dev.iagorodrigues.dscatalog.dto;

import dev.iagorodrigues.dscatalog.services.validation.user.UserUpdateValid;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO {
    public static final long serialVersionUID = 1L;

    public UserUpdateDTO() {
        super();
    }
}
