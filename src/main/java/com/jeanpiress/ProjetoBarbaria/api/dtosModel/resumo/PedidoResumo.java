package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResumo {

    private OffsetDateTime horario;
    private ClienteIdNome cliente;
    private BigDecimal comissaoGerada;
    private BigDecimal pontuacaoGerada;
    private BigDecimal valorTotal;
    private OffsetDateTime dataPagamento;
}
