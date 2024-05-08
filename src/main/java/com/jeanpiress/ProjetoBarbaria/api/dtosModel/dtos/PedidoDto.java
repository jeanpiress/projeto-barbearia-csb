package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ClienteIdNome;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProfissionalResumo;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.UsuarioResumo;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.StatusPedido;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PedidoDto {

    @ApiModelProperty(value = "ID do pedido", example = "1")
    private Long id;

    @ApiModelProperty(example = "2024-01-25T14:30:00-03:00")
    private OffsetDateTime horario;

    private List<ItemPedidoDto> itemPedidos = new ArrayList<>();

    @ApiModelProperty(example = "PAGO")
    private StatusPagamento statusPagamento;

    @ApiModelProperty(example = "DINHEIRO")
    private FormaPagamento formaPagamento;

    @ApiModelProperty(example = "ATENDIDO")
    private StatusPedido statusPedido;

    private ClienteIdNome cliente;

    private ProfissionalResumo profissional;

    @ApiModelProperty(value = "Comissao gerada pelo pedido", example = "45.00")
    private BigDecimal comissaoGerada;

    @ApiModelProperty(value = "Valor total", example = "90.00")
    private BigDecimal valorTotal;

    @ApiModelProperty(example = "true")
    private Boolean caixaAberto;

    @ApiModelProperty(value = "Pontuação gerada pelo pedido", example = "100")
    private BigDecimal pontuacaoGerada;

    private UsuarioResumo criadoPor;
    @ApiModelProperty(value ="Pedido criado no horario:", example = "2024-01-25T14:30:00-03:00")
    private OffsetDateTime criadoAs;

    private UsuarioResumo alteradoPor;
    @ApiModelProperty(value ="Pedido modificado no horario:", example = "2024-01-25T14:30:00-03:00")
    private OffsetDateTime modificadoAs;

    private UsuarioResumo recibidoPor;
    @ApiModelProperty(value ="Pedido pago no horario:", example = "2024-01-25T14:30:00-03:00")
    private OffsetDateTime dataPagamento;

    private UsuarioResumo canceladoPor;
    @ApiModelProperty(value ="Pedido cancelado no horario:", example = "2024-01-25T14:30:00-03:00")
    private OffsetDateTime canceladoAs;
}
