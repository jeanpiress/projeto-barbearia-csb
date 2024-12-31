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

    private LocalDate inicioPrimeiroMes;
    private LocalDate fimPrimeiroMes;
    private LocalDate inicioSegundoMes;
    private LocalDate fimSegundoMes;
}
