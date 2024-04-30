package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ClienteResumo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClientesRetorno {

    private ClienteResumo cliente;

    @ApiModelProperty(value = "Dias passados", example = "5")
    private Long diasPassados;

    @ApiModelProperty(value = "Previsão de retorno", example = "30")
    private String previsaoRetorno;


}
