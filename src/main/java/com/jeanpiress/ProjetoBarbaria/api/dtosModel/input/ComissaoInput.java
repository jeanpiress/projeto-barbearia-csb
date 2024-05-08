package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProdutoId;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProfissionalId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class ComissaoInput {


    @Valid
    @NotNull
    @ApiModelProperty(value = "ID do produto", example = "Corte", required = true)
    private ProdutoId produto;

    @Valid
    @NotNull
    @ApiModelProperty(value = "ID do profissional", example = "1", required = true)
    private ProfissionalId profissional;

    @NotNull
    @ApiModelProperty(value = "Porcentagem paga a quem vendeu", example = "50.00", required = true)
    private BigDecimal porcentagemComissao;
}

