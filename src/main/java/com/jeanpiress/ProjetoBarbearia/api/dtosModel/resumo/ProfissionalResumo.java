package com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfissionalResumo {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "João")
    private String nome;
}
