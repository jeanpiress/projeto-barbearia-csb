package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FormatoDataException extends NegocioException {
    private static final long serialVersionUID = 1L;

    public FormatoDataException(String message) {
        super(message);
    }
}
