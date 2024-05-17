package com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Builder
@Getter
@Setter
public class DataInicioFim {

    private OffsetDateTime inicio;
    private OffsetDateTime fim;
}
