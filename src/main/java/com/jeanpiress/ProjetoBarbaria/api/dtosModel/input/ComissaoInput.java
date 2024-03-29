package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProdutoId;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProfissionalId;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class ComissaoInput {

    @NotNull
    private ProdutoId produto;
    @NotNull
    @Valid
    private ProfissionalId profissional;
    @NotNull
    private BigDecimal porcentagemComissao;
}

