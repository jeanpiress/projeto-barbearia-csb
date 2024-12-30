package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

public class PedidoSemProfissionalException extends NegocioException{
    public PedidoSemProfissionalException(String mensagem) {
        super(mensagem);
    }

    public PedidoSemProfissionalException(Long pedidoId) {
        this(String.format("Não existe profissional definido para o pedio %d", pedidoId));
    }
}
