package com.jeanpiress.ProjetoBarbaria.api.controller.dtos;

import com.jeanpiress.ProjetoBarbaria.domain.model.Categoria;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoDto {

    private Long id;
    private String nome;
    private BigDecimal preco;
    private Integer estoque;
    private boolean vendidoPorPonto = false;
    private BigDecimal pesoPontuacaoCliente;
    private BigDecimal pesoPontuacaoProfissional;
    private BigDecimal precoEmPontos;
    private BigDecimal comissaoBase;
    private Categoria categoria;
}
