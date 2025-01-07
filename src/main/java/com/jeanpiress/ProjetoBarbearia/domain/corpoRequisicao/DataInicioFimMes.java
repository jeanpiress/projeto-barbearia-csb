package com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Builder
@Getter
@Setter
public class DataInicioFimMes {

    private LocalDateTime inicioPrimeiroMes;
    private LocalDateTime fimPrimeiroMes;
    private LocalDateTime inicioSegundoMes;
    private LocalDateTime fimSegundoMes;
}
