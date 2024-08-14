package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProdutoId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemPedidoInput {

    @NotNull
    @Schema(example = "2", required = true)
    private Integer quantidade;

    @NotNull
    @Valid
    @Schema(description = "ID do produto", example = "1", required = true)
    private ProdutoId produto;
}
