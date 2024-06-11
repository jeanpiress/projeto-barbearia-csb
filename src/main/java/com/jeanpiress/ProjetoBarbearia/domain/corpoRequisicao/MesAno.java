package com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MesAno {

    @NotNull
    @Schema(example = "2024-05-06T00:00:00-03:00", required = true)
    private OffsetDateTime data;

}
