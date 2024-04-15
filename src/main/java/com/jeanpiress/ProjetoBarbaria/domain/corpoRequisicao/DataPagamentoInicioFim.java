package com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@Builder
@Getter
@Setter
public class DataPagamentoInicioFim {

    private OffsetDateTime inicio;
    private OffsetDateTime fim;
}
