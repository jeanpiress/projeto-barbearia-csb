package com.jeanpiress.ProjetoBarbaria.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaixaDiario {

    private BigDecimal dinheiro;
    private BigDecimal pix;
    private BigDecimal credito;
    private BigDecimal debito;
    private BigDecimal voucher;
    private BigDecimal pontos;
    private BigDecimal total;

}
