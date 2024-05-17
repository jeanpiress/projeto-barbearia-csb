package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ConferenciaSenhaException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ConferenciaSenhaException(String mensagem) {
        super(mensagem);
    }

    public ConferenciaSenhaException() {
        this("Confirme sua senha corretamente");
    }
}
