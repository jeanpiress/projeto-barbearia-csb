package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException{
    public PedidoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public PedidoNaoEncontradoException(Long pedidoId) {
        this(String.format("Não existe um cadastro de pedido com codigo %d", pedidoId));
    }
}
