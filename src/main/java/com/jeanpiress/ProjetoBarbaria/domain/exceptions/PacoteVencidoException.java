package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

public class PacoteVencidoException extends EntidadeNaoEncontradaException{
    public PacoteVencidoException(String mensagem) {
        super(mensagem);
    }

    public PacoteVencidoException(Long pacoteId) {
        this(String.format("O pacote de codigo %d não esta vencido", pacoteId));
    }
}
