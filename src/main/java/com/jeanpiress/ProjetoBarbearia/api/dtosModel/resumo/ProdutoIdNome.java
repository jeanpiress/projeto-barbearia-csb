package com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoIdNome {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "Corte")
    private String nome;
}
