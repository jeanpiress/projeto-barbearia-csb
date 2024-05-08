package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Builder
public class ProfissionalIdNome {

    @ApiModelProperty(example = "1")
    private Long id;
    @ApiModelProperty(example = "João")
    private String nome;
}
