package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ClienteResumo;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProfissionalResumo;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.StatusPedido;
import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PedidoDto {

    private Long id;
    private OffsetDateTime horario;
    private List<ItemPedido> itemPedidos = new ArrayList<>();
    private StatusPagamento statusPagamento;
    private FormaPagamento formaPagamento;
    private StatusPedido statusPedido;
    private ClienteResumo cliente;
    private ProfissionalResumo profissional;
    private BigDecimal comissaoGerada;
    private BigDecimal valorTotal;
    private Boolean caixaAberto;

}