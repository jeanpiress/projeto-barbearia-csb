package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemPedidoResumo {

    private Long id;
    private BigDecimal precoUnitario;
    private ProdutoIdNome produto;

}
