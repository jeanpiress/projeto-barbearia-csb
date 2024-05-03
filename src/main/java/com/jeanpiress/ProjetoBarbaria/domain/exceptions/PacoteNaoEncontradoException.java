package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

public class PacoteNaoEncontradoException extends EntidadeNaoEncontradaException{
    public PacoteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public PacoteNaoEncontradoException(Long pacoteId) {
        this(String.format("Não existe um cadastro de pacote com codigo %d", pacoteId));
    }
}
