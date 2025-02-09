package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoAlteracaoInput {

    @NotNull
    @Schema(example = "2024-04-19T14:30:00-03:00", required = true)
    private OffsetDateTime horario;

    @NotNull
    @Valid
    @Schema(description = "ID do profissional", example = "1", required = true)
    private ProfissionalId profissional;

    @Schema(description = "Descrição do serviço", example = "corte")
    private String descricao;

    @Schema(description = "Duracao do serviço", example = "01:00")
    private String duracao;
}
