package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

public class PermissaoInvalidaException extends NegocioException{
    public PermissaoInvalidaException(String mensagem) {
        super(mensagem);
    }

}
