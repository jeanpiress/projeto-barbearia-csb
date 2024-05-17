package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProdutoId;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalId;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "ID do produto", example = "Corte", required = true)
    private ProdutoId produto;

    @Valid
    @NotNull
    @Schema(description = "ID do profissional", example = "1", required = true)
    private ProfissionalId profissional;

    @NotNull
    @Schema(description = "Porcentagem paga a quem vendeu", example = "50.00", required = true)
    private BigDecimal porcentagemComissao;
}

