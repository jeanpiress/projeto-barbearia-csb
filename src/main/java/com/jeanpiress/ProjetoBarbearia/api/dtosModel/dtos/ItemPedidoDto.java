package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProdutoResumo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ItemPedidoDto {

    @Schema(description = "ID do item", example = "1")
    private Long id;

    @Schema(description = "Preço unitario", example = "45.00")
    private BigDecimal precoUnitario;

    @Schema(description = "Preço total", example = "90.00")
    private BigDecimal precoTotal;

    @Schema(example = "2")
    private Integer quantidade;

    @Schema(description = "Corte de cabelo", example = "5")
    private String observacao;

    private ProdutoResumo produto;
}
