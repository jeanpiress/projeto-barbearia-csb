package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

public class ItemPacoteNaoEncontradoException extends EntidadeNaoEncontradaException{
    public ItemPacoteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public ItemPacoteNaoEncontradoException(Long itemPacoteId) {
        this(String.format("Não existe um cadastro de itemPacote com codigo %d", itemPacoteId));
    }
}
