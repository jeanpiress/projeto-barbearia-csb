package com.jeanpiress.ProjetoBarbearia.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComparacaoMes {

    private LocalDate dataInicio;
    private LocalDate dataFim;
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
