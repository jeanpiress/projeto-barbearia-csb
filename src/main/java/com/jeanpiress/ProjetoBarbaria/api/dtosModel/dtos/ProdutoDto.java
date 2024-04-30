package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.domain.model.Categoria;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoDto {

    @ApiModelProperty(value = "ID do produto", example = "1")
    private Long id;

    @ApiModelProperty(example = "Corte")
    private String nome;

    @ApiModelProperty(example = "45.00")
    private BigDecimal preco;

    @ApiModelProperty(example = "null")
    private Integer estoque;

    @ApiModelProperty(example = "false")
    private boolean vendidoPorPonto = false;

    @ApiModelProperty(example = "1.50")
    private BigDecimal pesoPontuacaoCliente;

    @ApiModelProperty(example = "2.50")
    private BigDecimal pesoPontuacaoProfissional;

    @ApiModelProperty(example = "null")
    private BigDecimal precoEmPontos;

    @ApiModelProperty(example = "50.00")
    private BigDecimal comissaoBase;

    private Categoria categoria;
}
