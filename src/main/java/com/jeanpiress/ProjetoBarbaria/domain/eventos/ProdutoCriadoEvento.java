package com.jeanpiress.ProjetoBarbaria.domain.eventos;


import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;

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
