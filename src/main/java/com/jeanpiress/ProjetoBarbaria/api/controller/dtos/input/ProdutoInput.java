package com.jeanpiress.ProjetoBarbaria.api.controller.dtos.input;

import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.resumo.CategoriaID;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoInput {

    @NotBlank
    private String nome;
    @Positive
    private BigDecimal preco;
    private boolean temEstoque;
    private Integer estoque;
    private boolean vendidoPorPonto = false;
    private BigDecimal pesoPontuacaoCliente;
    private BigDecimal pesoPontuacaoProfissional;
    private BigDecimal precoEmPontos;
    @NotNull
    private BigDecimal comissaoBase;
    @NotNull
    private CategoriaID categoria;
}
