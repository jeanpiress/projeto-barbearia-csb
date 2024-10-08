package com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ItemPacoteId;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.PacoteId;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalId;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RealizacaoItemPacote {

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
