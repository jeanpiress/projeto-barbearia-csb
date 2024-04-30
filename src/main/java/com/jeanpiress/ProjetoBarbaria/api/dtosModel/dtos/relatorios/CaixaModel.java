package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Caixa")
public class CaixaModel {

    @ApiModelProperty(example = "100.00")
    private BigDecimal dinheiro;

    @ApiModelProperty(example = "200.00")
    private BigDecimal pix;

    @ApiModelProperty(example = "300.00")
    private BigDecimal credito;

    @ApiModelProperty(example = "400.00")
    private BigDecimal debito;

    @ApiModelProperty(example = "500.00")
    private BigDecimal voucher;

    @ApiModelProperty(example = "600.00")
    private BigDecimal pontos;

    @ApiModelProperty(example = "1000.00")
    private BigDecimal total;

    @ApiModelProperty(value = "Quantidade de clientes atendidos", example = "5")
    private Integer clientesAtendidos;

    @ApiModelProperty(value = "Quantidade de produtos vendidos" ,example = "10")
    private Integer quantidadeProdutosVendidos;

}
