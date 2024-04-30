package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaDto {

    @ApiModelProperty(value = "ID da categoria", example = "1")
    private Long id;

    @ApiModelProperty(example = "Cabelo")
    private String nome;
}
