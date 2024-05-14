package com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DataJson {

    @NotBlank
    @ApiModelProperty(example = "2024-05-06", required = true)
    private String inicio;
    @NotBlank
    @ApiModelProperty(example = "2024-05-06", required = true)
    private String fim;
}
