package com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class DataJsonInicioFim {

    @NotBlank
    @Schema(example = "2024-05-06", required = true)
    private String inicio;
    @NotBlank
    @Schema(example = "2024-05-06", required = true)
    private String fim;
}
