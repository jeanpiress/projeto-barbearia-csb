package com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ItemPacoteResumo {

    @Schema(example = "1")
    private Long id;
    private ItemPedidoResumo itemPedido;
    private ProfissionalIdNome profissional;
    @Schema(example = "2024-04-01T00:00:00Z")
    private OffsetDateTime dataConsumo;

}
