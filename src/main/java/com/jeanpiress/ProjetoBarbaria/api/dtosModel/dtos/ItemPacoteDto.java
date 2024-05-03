package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ItemPedidoResumo;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProfissionalIdNome;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ItemPacoteDto {

    private Long id;

    private ItemPedidoResumo itemPedido;

    private ProfissionalIdNome profissional;

    private OffsetDateTime dataConsumo;
}
