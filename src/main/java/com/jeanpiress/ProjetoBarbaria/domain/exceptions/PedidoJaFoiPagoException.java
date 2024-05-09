package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

public class PedidoJaFoiPagoException extends NegocioException{
    public PedidoJaFoiPagoException(String mensagem) {
        super(mensagem);
    }

    public PedidoJaFoiPagoException(Long pedidoId) {
        this(String.format("O pedido de codigo %d já consta como pago", pedidoId));
    }
}
