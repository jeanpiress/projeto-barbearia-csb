package com.jeanpiress.ProjetoBarbaria.domain.model.relatorios;

import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioComissao {

    private Profissional profissional;
    private BigDecimal totalVendas;
    private BigDecimal totalComissao;
    private BigDecimal totalpontos;
    private BigDecimal tkm;
    private Integer clienteAtendidos;

}
