package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProdutoId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class ItemPedidoInput {

    @NotNull
    @ApiModelProperty(example = "2", required = true)
    private Integer quantidade;

    @NotNull
    @Valid
    @ApiModelProperty(value = "ID do produto", example = "1", required = true)
    private ProdutoId produto;
}
