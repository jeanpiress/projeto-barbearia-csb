package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProdutoIdNome;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProfissionalIdNome;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ComissaoDto {

    @ApiModelProperty(value = "ID da comissao", example = "1")
    private Long id;

    @ApiModelProperty(example = "Corte")
    private ProdutoIdNome produto;

    private ProfissionalIdNome profissional;

    @ApiModelProperty(value = "Porcentagem paga de comissao", example = "50.00")
    private BigDecimal porcentagemComissao;

}
