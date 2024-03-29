package com.jeanpiress.ProjetoBarbaria.api.controller.dtos;

import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ItemPedidoDto {

    private Long id;
    private BigDecimal precoUnitario;
    private BigDecimal precoTotal;
    private Integer quantidade;
    private String observacao;
    private Produto produto;
}
