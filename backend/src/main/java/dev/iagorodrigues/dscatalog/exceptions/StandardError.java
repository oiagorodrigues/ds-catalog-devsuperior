package dev.iagorodrigues.dscatalog.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
public class StandardError extends CustomError {
    private static final long serialVersionUID = 1L;

    private String message;

}
