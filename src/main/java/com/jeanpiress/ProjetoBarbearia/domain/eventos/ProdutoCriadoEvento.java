package com.jeanpiress.ProjetoBarbearia.domain.eventos;


import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;

public class ProdutoCriadoEvento {

    private Produto produto;

    public ProdutoCriadoEvento(Produto produto) {
        super();
        this.produto = produto;
    }

    public Produto getProduto() {
        return produto;
    }
}
