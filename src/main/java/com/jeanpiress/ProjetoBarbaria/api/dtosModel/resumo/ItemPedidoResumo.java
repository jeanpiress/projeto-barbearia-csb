package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemPedidoResumo {

    @ApiModelProperty(example = "1")
    private Long id;
    @ApiModelProperty(example = "2024-04-01T00:00:00Z")
    private BigDecimal precoUnitario;
    private ProdutoIdNome produto;

}
