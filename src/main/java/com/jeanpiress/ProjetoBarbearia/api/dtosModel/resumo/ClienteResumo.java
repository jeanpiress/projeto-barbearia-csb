package com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteResumo {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "João")
    private String nome;
    @Schema(example = "34999999999")
    private String celular;
    @Schema(example = "30")
    private Integer diasRetorno;
    
}
