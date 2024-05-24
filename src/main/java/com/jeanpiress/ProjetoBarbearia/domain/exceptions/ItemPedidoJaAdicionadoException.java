package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ItemPedidoJaAdicionadoException extends NegocioException {
    private static final long serialVersionUID = 1L;

    public ItemPedidoJaAdicionadoException(String message) {
        super(message);
    }

    public ItemPedidoJaAdicionadoException(Long ItemPedidoId) {
        this(String.format("O itemPedido de codigo %d já foi adicionado a esse pedido", ItemPedidoId));
    }
}
