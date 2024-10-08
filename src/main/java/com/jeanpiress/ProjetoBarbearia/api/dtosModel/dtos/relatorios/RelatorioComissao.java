package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios;

import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioComissao {


    private Profissional profissional;

    @Schema(example = "1000.00")
    private BigDecimal totalVendas;

    @Schema(example = "500.00")
    private BigDecimal totalComissao;

    @Schema(example = "5000.00")
    private BigDecimal totalpontos;

    @Schema(example = "100.00")
    private BigDecimal tkm;

    @Schema(example = "10")
    private Integer clienteAtendidos;

}
