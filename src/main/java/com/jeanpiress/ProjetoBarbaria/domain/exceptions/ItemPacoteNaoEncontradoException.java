package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

public class ItemPacoteNaoEncontradoException extends EntidadeNaoEncontradaException{
    public ItemPacoteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public ItemPacoteNaoEncontradoException(Long itemPacoteId) {
        this(String.format("Não existe um cadastro de item com codigo %d", itemPacoteId));
    }
}
