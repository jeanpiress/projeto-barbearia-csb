package com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfissionalResumo {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "João")
    private String nome;
}
