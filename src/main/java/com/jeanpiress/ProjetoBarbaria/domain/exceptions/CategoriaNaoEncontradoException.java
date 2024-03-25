package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

public class CategoriaNaoEncontradoException extends EntidadeNaoEncontradaException{
    public CategoriaNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public CategoriaNaoEncontradoException(Long categoriaID) {
        this(String.format("Não existe um cadastro de categoria com codigo %d", categoriaID));
    }
}
