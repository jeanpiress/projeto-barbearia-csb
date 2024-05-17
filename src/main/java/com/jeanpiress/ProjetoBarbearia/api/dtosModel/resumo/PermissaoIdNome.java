package com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissaoIdNome {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "GERENTE")
    private String nome;
}
