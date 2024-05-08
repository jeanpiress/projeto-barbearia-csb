package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfissionalIdNome {

    @ApiModelProperty(example = "1")
    private Long id;
    @ApiModelProperty(example = "João")
    private String nome;
}
