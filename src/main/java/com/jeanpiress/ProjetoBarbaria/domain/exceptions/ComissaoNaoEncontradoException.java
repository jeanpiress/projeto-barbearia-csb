package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

public class ComissaoNaoEncontradoException extends EntidadeNaoEncontradaException{
    public ComissaoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public ComissaoNaoEncontradoException(Long comissaoId) {
        this(String.format("Não existe um cadastro de comissao com codigo %d", comissaoId));
    }
}
