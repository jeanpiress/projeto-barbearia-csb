package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

public class PedidoNaoPodeSerConfirmadoException extends NegocioException{
    public PedidoNaoPodeSerConfirmadoException(String mensagem) {
        super(mensagem);
    }

    public PedidoNaoPodeSerConfirmadoException(Long pedidoId) {
        this(String.format("O pedido de codigo %d não pode ser confirmado pois já foi finalizado ou cancelado", pedidoId));
    }
}
