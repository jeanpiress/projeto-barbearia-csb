package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoResumo {

    @ApiModelProperty(example = "1")
    private Long id;
    @ApiModelProperty(example = "Corte")
    private String nome;
    @ApiModelProperty(example = "45.00")
    private BigDecimal preco;
    @ApiModelProperty(example = "1500")
    private BigDecimal precoEmPontos;
    private CategoriaIDNome categoria;
}
