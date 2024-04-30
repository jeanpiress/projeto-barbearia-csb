package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailExistenteException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EmailExistenteException(String mensagem) {
        super(mensagem);
    }

    public EmailExistenteException() {

        this("Email já cadastrado");
    }
}
