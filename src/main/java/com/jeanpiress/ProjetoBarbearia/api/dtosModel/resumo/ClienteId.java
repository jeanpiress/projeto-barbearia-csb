package com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteId {

    @Schema(example = "1", required = true)
    private Long id;
}
