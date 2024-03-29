package com.jeanpiress.ProjetoBarbaria.api.controller.dtos.input;

import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.resumo.ProdutoId;
import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.resumo.ProfissionalId;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class ComissaoInput {

    @NotNull
    private ProdutoId produto;
    @NotNull
    private ProfissionalId profissional;
    @NotNull
    private BigDecimal porcentagemComissao;
}

