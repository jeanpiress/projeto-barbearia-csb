package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProdutoId;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class ItemPedidoInput {

    @NotNull
    private Integer quantidade;
    @NotNull
    @Valid
    private ProdutoId produto;
}
