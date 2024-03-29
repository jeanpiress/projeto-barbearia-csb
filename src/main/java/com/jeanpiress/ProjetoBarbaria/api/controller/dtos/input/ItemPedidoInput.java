package com.jeanpiress.ProjetoBarbaria.api.controller.dtos.input;

import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.resumo.ProdutoId;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
public class ItemPedidoInput {

    @NotNull
    private Integer quantidade;
    @NotNull
    private ProdutoId produto;
}
