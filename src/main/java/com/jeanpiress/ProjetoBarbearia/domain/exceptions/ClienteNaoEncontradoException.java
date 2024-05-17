package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

public class ClienteNaoEncontradoException extends EntidadeNaoEncontradaException{
    public ClienteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public ClienteNaoEncontradoException(Long clienteId) {
        this(String.format("Não existe um cadastro de cliente com codigo %d", clienteId));
    }
}
