package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.PedidoResumo;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProfissionalIdNome;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioComissaoDetalhada {


    private ProfissionalIdNome profissional;

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

    private List<PedidoResumo> pedidos;
}
