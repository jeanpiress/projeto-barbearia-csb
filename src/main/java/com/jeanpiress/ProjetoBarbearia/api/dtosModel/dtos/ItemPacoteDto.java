package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ItemPedidoResumo;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalIdNome;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ItemPacoteDto {

    @Schema(description = "ID do itemPacote", example = "1")
    private Long id;

    private ItemPedidoResumo itemPedido;

    private ProfissionalIdNome profissional;

    private OffsetDateTime dataConsumo;
}
