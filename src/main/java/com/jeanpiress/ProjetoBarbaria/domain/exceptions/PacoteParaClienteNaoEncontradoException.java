package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

public class PacoteParaClienteNaoEncontradoException extends EntidadeNaoEncontradaException{
    public PacoteParaClienteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public PacoteParaClienteNaoEncontradoException(Long clienteId) {
        this(String.format("Não existe um pacote ativo para o clente de codigo %d", clienteId));
    }
}
