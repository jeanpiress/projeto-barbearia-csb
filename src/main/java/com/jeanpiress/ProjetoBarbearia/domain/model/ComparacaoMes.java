package com.jeanpiress.ProjetoBarbearia.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
public class ComparacaoMes {

    private OffsetDateTime dataInicio;
    private OffsetDateTime dataFim;
    private BigDecimal faturamentoPrimeiroMes;
    private BigDecimal faturamentoSegundoMes;
    private BigDecimal diferencaFaturamento;
    private Integer clientesAtendidosPrimeiroMes;
    private Integer clientesAtendidosSegundoMes;
    private Integer diferencaClientesAtendidos;
    private BigDecimal tkmPrimeiroMes;
    private BigDecimal tkmSegundoMes;
    private BigDecimal diferencaTkm;
}
