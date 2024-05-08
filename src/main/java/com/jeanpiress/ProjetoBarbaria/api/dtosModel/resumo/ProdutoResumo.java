package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoResumo {

    private Long id;
    private String nome;
    private BigDecimal preco;
    private BigDecimal precoEmPontos;
    private CategoriaIDNome categoria;
}
