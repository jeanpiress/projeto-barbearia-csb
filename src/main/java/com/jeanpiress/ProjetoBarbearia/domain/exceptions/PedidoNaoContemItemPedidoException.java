package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PedidoNaoContemItemPedidoException extends NegocioException {
    private static final long serialVersionUID = 1L;

    public PedidoNaoContemItemPedidoException(String message) {
        super(message);
    }

    public PedidoNaoContemItemPedidoException(Long pedidoId, Long ItemPedidoId) {
        this(String.format("O pedido  codigo %d não possui o item pedido de codigo %d", pedidoId, ItemPedidoId));
    }
}
