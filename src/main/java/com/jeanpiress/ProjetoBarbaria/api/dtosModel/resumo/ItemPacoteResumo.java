package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ItemPacoteResumo {

    @ApiModelProperty(example = "1")
    private Long id;
    private ItemPedidoResumo itemPedido;
    private ProfissionalIdNome profissional;
    @ApiModelProperty(example = "2024-04-01T00:00:00Z")
    private OffsetDateTime dataConsumo;

}
