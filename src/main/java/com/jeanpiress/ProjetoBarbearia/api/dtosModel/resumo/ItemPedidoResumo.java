package com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemPedidoResumo {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "2024-04-01T00:00:00Z")
    private BigDecimal precoUnitario;
    private ProdutoIdNome produto;

}
