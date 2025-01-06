package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Tag(name = "Faturamento_dia")
public class FaturamentoDia {

    @Schema(example = "2024-12-30")
    private LocalDate data;

    @Schema(example = "1000.00")
    private BigDecimal total;

    @Schema(example = "100.00")
    private BigDecimal tkm;

    @Schema(description = "Quantidade de clientes atendidos", example = "5")
    private Integer clientesAtendidos;

}
