package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

public class ProfissionalNaoEncontradoException extends EntidadeNaoEncontradaException{
    public ProfissionalNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public ProfissionalNaoEncontradoException(Long clienteId) {
        this(String.format("Não existe um cadastro de profissional com codigo %d", clienteId));
    }
}
