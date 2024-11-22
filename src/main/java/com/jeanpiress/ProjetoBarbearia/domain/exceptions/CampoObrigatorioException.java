package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CampoObrigatorioException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public CampoObrigatorioException(String mensagem) {
        super(mensagem);
    }

    public CampoObrigatorioException() {
        this("Algum campo obrigatorio não foi preenchido");
    }
}
