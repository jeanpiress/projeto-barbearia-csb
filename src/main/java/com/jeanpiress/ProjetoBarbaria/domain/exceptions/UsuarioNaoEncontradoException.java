package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

public class UsuarioNaoEncontradoException extends EntidadeNaoEncontradaException{
    public UsuarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public UsuarioNaoEncontradoException(Long usuarioId) {
        this(String.format("Não existe um cadastro de usuario com codigo %d", usuarioId));
    }

    public UsuarioNaoEncontradoException() {
        this("Não existe um cadastro de usuario com este email");
    }
}
