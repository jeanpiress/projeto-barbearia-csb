package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ClienteResumo {

    @ApiModelProperty(example = "1")
    private Long id;
    @ApiModelProperty(example = "João")
    private String nome;
    @ApiModelProperty(example = "34999999999")
    private String celular;
    @ApiModelProperty(example = "30")
    private Integer diasRetorno;
    
}
