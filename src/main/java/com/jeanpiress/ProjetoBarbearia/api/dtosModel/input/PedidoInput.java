package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ClienteId;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
public class PedidoInput {

    @NotNull
    @Schema(example = "2024-04-19T14:30:00-03:00", required = true)
    private OffsetDateTime horario;

    @Schema(description = "ID do Profissional", example = "1", required = true)
    private ClienteId cliente;

    @NotNull
    @Valid
    @Schema(description = "ID do Profissional", required = true)
    private ProfissionalId profissional;
}
