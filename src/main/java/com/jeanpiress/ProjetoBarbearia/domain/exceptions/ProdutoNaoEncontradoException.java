package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

public class ProdutoNaoEncontradoException extends EntidadeNaoEncontradaException{
    public ProdutoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public ProdutoNaoEncontradoException(Long clienteId) {
        this(String.format("Não existe um cadastro de produto com codigo %d", clienteId));
    }
}
