package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ItemPacoteResumo {

    private Long id;
    private ItemPedidoResumo itemPedido;
    private ProfissionalIdNome profissional;
    private OffsetDateTime dataConsumo;

}
