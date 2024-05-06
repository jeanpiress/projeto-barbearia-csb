package com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ItemPacoteId;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.PacoteId;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProfissionalId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Builder
@Getter
@Setter
public class RealiazacaoItemPacote {

    @NotNull
    private ProfissionalId profissional;
    @NotNull
    private PacoteId pacote;
    @NotNull
    private ItemPacoteId itemPacote;
}
