package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

public class PacoteVencidoException extends EntidadeNaoEncontradaException{
    public PacoteVencidoException(String mensagem) {
        super(mensagem);
    }

    public PacoteVencidoException(Long pacoteId) {
        this(String.format("O pacote de codigo %d esta vencido", pacoteId));
    }
}
