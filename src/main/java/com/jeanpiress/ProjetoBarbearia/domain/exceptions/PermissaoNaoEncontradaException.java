package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

public class PermissaoNaoEncontradaException extends EntidadeNaoEncontradaException{
    public PermissaoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public PermissaoNaoEncontradaException(Long permissaoId) {
        this(String.format("Não existe um cadastro de permissao com codigo %d", permissaoId));
    }
}
