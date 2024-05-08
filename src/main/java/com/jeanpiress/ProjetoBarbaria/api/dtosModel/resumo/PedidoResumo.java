package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResumo {

    @ApiModelProperty(example = "2024-04-01T00:00:00Z")
    private OffsetDateTime horario;
    private ClienteIdNome cliente;
    @ApiModelProperty(example = "22.50")
    private BigDecimal comissaoGerada;
    @ApiModelProperty(example = "45.00")
    private BigDecimal pontuacaoGerada;
    @ApiModelProperty(example = "45.00")
    private BigDecimal valorTotal;
    @ApiModelProperty(example = "2024-04-01T00:00:00Z")
    private OffsetDateTime dataPagamento;
}
