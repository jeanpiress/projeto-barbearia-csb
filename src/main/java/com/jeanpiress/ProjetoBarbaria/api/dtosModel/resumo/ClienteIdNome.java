package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteIdNome {

    @ApiModelProperty(example = "1")
    private Long id;
    @ApiModelProperty(example = "João")
    private String nome;
}
