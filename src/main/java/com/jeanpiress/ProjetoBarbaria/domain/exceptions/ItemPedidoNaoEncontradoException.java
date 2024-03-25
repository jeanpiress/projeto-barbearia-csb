package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

public class ItemPedidoNaoEncontradoException extends EntidadeNaoEncontradaException{
    public ItemPedidoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public ItemPedidoNaoEncontradoException(Long categoriaID) {
        this(String.format("Não existe um cadastro de item com codigo %d", categoriaID));
    }
}
