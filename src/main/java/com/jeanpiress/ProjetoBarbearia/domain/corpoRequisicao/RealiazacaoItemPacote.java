package com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ItemPacoteId;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.PacoteId;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
public class RealiazacaoItemPacote {

    @NotNull
    @Valid
    private ProfissionalId profissional;
    @NotNull
    @Valid
    private PacoteId pacote;
    @NotNull
    @Valid
    private ItemPacoteId itemPacote;
}
