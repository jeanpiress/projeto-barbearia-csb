package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CategoriaInput {

    @ApiModelProperty(example = "Cabelo", required = true)
    @NotNull
    private String nome;
}
