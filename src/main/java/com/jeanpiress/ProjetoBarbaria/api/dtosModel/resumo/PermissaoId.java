package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PermissaoId {

    @NotNull
    @ApiModelProperty(example = "1", required = true)
    private Long id;
}