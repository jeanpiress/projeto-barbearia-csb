package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios;

import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientesRetorno {

    private Cliente cliente;

    @Schema(description = "Dias passados", example = "5")
    private Long diasPassadosRetorno;

    @Schema(description = "Previsão de retorno")
    private LocalDate previsaoRetorno;


}
