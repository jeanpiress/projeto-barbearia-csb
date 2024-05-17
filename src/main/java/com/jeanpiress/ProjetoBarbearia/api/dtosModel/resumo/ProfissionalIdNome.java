package com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfissionalIdNome {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "João")
    private String nome;
}
