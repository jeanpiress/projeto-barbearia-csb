package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SenhaAtualIncorretaException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public SenhaAtualIncorretaException(String mensagem) {
        super(mensagem);
    }

    public SenhaAtualIncorretaException() {
        this("Senha atual não esta correta");
    }
}
