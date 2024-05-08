package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CategoriaIDNome {

    @NotNull
    @ApiModelProperty(example = "1")
    private Long id;
    @ApiModelProperty(example = "Cabelo")
    private String nome;
}
