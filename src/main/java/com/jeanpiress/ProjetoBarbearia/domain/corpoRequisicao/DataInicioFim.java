package com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataInicioFim {

    @NotNull
    @Schema(example = "2024-05-06T00:00:00-03:00", required = true)
    private OffsetDateTime inicio;

    @NotNull
    @Schema(example = "2024-05-06T00:00:00-03:00", required = true)
    private OffsetDateTime fim;
}
