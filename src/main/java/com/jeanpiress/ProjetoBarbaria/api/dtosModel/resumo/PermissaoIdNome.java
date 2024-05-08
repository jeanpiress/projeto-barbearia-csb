package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissaoIdNome {

    @ApiModelProperty(example = "1")
    private Long id;
    @ApiModelProperty(example = "GERENTE")
    private String nome;
}
