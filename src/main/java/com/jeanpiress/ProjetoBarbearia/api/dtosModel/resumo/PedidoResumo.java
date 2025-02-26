package com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PedidoResumo {

    @Schema(example = "2024-04-01T00:00:00Z")
    private OffsetDateTime horario;

    private ClienteIdNome cliente;

    @Schema(example = "22.50")
    private BigDecimal comissaoGerada;

    @Schema(example = "45.00")
    private BigDecimal pontuacaoProfissionalGerada;

    @Schema(example = "45.00")
    private BigDecimal pontuacaoClienteGerada;

    @Schema(example = "45.00")
    private BigDecimal valorTotal;

    @Schema(example = "2024-04-01T00:00:00Z")
    private LocalDateTime dataPagamento;
}
