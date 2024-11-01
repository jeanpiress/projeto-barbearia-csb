package com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalId;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FiltroPedido {

    @NotNull
    @Schema(example = "1")
    private StatusPedido statusPedido;

    @NotNull
    @Schema(example = "1")
    private ProfissionalId profissionalId;

}
