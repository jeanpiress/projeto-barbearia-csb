package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.CategoriaID;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoInput {

    @NotBlank
    @ApiModelProperty(example = "Corte", required = true)
    private String nome;

    @Positive
    @NotNull
    @ApiModelProperty(value = "Preco do produto", example = "45.00", required = true)
    private BigDecimal preco;

    @ApiModelProperty(value = "Tem estoque", example = "false")
    private boolean temEstoque;

    @ApiModelProperty(value = "Quantidade de item em estoque", example = "20")
    private Integer estoque;

    @ApiModelProperty(value = "Vendido por ponto", example = "false")
    private boolean vendidoPorPonto = false;

    @ApiModelProperty(value = "Peso da pontuação para o cliente", example = "1.50")
    private BigDecimal pesoPontuacaoCliente;

    @ApiModelProperty(value = "Peso da pontuação para o profissional", example = "2.50")
    private BigDecimal pesoPontuacaoProfissional;

    @ApiModelProperty(value = "Preço em pontos", example = "500000.00")
    private BigDecimal precoEmPontos;

    @NotNull
    @ApiModelProperty(value = "Comissao padrão do produto", example = "50.00", required = true)
    private BigDecimal comissaoBase;

    @NotNull
    @Valid
    private CategoriaID categoria;
}
