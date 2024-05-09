package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PacoteProntoIdNome {

    @ApiModelProperty(example = "1")
    private Long id;
    @ApiModelProperty(example = "4 barbas")
    private String nome;
}
