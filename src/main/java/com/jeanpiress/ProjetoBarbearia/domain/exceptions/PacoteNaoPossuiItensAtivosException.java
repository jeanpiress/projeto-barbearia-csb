package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PacoteNaoPossuiItensAtivosException extends NegocioException {
    private static final long serialVersionUID = 1L;

    public PacoteNaoPossuiItensAtivosException(String message) {
        super(message);
    }

    public PacoteNaoPossuiItensAtivosException(Long pacoteId) {
        this(String.format("O pacote de codigo %d não possui itens ativos", pacoteId));
    }
}
