package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ClienteIdNome;
import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientesRetornoDto {

    private ClienteIdNome cliente;

    @Schema(description = "Dias passados", example = "5")
    private Long diasPassados;

    @Schema(description = "Previsão de retorno")
    private OffsetDateTime previsaoRetorno;


}
