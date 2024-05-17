package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PedidoNaoPodeSerCanceladoException extends NegocioException{
    public PedidoNaoPodeSerCanceladoException(String mensagem) {
        super(mensagem);
    }

}
