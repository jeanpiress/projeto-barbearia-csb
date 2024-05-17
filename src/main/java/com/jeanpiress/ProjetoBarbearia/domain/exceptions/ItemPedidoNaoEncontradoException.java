package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

public class ItemPedidoNaoEncontradoException extends EntidadeNaoEncontradaException{
    public ItemPedidoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public ItemPedidoNaoEncontradoException(Long itemPedidoId) {
        this(String.format("Não existe um cadastro de item com codigo %d", itemPedidoId));
    }
}
