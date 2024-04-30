package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ItemPedidoDto {

    @ApiModelProperty(value = "ID do item", example = "1")
    private Long id;

    @ApiModelProperty(value = "Preço unitario", example = "45.00")
    private BigDecimal precoUnitario;

    @ApiModelProperty(value = "Preço total", example = "90.00")
    private BigDecimal precoTotal;

    @ApiModelProperty(example = "2")
    private Integer quantidade;

    @ApiModelProperty(value = "Corte de cabelo", example = "5")
    private String observacao;

    private Produto produto;
}
