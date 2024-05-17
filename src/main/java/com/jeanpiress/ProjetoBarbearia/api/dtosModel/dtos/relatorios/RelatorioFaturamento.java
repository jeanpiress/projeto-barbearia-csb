package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioFaturamento {

    @Schema(example = "100.00")
    private BigDecimal dinheiro;

    @Schema(example = "200.00")
    private BigDecimal pix;

    @Schema(example = "300.00")
    private BigDecimal credito;

    @Schema(example = "400.00")
    private BigDecimal debito;

    @Schema(example = "500.00")
    private BigDecimal voucher;

    @Schema(example = "600.00")
    private BigDecimal pontos;

    @Schema(example = "1000.00")
    private BigDecimal total;

    @Schema(example = "200.00")
    private BigDecimal tkmLoja;

    @Schema(description = "Quantidade de clientes atendidos", example = "5")
    private Integer clientesAtendidos;

    @Schema(description = "Quantidade de produtos vendidos" ,example = "10")
    private Integer quantidadeProdutosVendidos;

}
