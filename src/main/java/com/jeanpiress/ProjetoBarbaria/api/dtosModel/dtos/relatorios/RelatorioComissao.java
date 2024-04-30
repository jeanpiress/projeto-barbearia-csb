package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios;

import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioComissao {


    private Profissional profissional;

    @ApiModelProperty(example = "1000.00")
    private BigDecimal totalVendas;

    @ApiModelProperty(example = "500.00")
    private BigDecimal totalComissao;

    @ApiModelProperty(example = "5000.00")
    private BigDecimal totalpontos;

    @ApiModelProperty(example = "100.00")
    private BigDecimal tkm;

    @ApiModelProperty(example = "10")
    private Integer clienteAtendidos;

}
