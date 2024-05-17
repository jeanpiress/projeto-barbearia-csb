package com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Builder
@Getter
@Setter
public class DataInicioFimMes {

    private OffsetDateTime inicioPrimeiroMes;
    private OffsetDateTime fimPrimeiroMes;
    private OffsetDateTime inicioSegundoMes;
    private OffsetDateTime fimSegundoMes;
}
