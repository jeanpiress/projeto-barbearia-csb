package com.jeanpiress.ProjetoBarbaria.domain.exceptions;

public class ItemPacoteNaoEncontradoEmItemAtivoException extends EntidadeNaoEncontradaException{
    public ItemPacoteNaoEncontradoEmItemAtivoException(String mensagem) {
        super(mensagem);
    }

    public ItemPacoteNaoEncontradoEmItemAtivoException(Long itemPacoteId) {
        this(String.format("Não existe um itemPacote de codigo %d listado em itensAtivos", itemPacoteId));
    }
}
