package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ProfissionalIdNome {

    @ApiModelProperty(example = "1")
    private Long id;
    @ApiModelProperty(example = "João")
    private String nome;
}
