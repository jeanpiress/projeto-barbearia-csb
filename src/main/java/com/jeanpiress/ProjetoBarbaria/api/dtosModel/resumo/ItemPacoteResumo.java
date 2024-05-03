package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ItemPacoteResumo {

    private ItemPedidoResumo itemPedido;
    private ProfissionalIdNome profissional;
    private OffsetDateTime dataConsumo;

}
