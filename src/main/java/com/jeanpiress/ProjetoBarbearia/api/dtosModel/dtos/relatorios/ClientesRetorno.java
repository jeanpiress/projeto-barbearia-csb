package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ClienteResumo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClientesRetorno {

    private ClienteResumo cliente;

    @Schema(description = "Dias passados", example = "5")
    private Long diasPassados;

    @Schema(description = "Previsão de retorno", example = "30")
    private String previsaoRetorno;


}
