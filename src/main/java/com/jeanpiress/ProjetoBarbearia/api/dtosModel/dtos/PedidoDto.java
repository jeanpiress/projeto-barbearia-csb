package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ClienteIdNome;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalResumo;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.UsuarioResumo;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PedidoDto {

    @Schema(description = "ID do pedido", example = "1")
    private Long id;

    @Schema(example = "2024-01-25T14:30:00-03:00")
    private OffsetDateTime horario;

    private List<ItemPedidoDto> itemPedidos = new ArrayList<>();

    @Schema(example = "PAGO")
    private StatusPagamento statusPagamento;

    @Schema(example = "DINHEIRO")
    private FormaPagamento formaPagamento;

    @Schema(example = "ATENDIDO")
    private StatusPedido statusPedido;

    private ClienteDto cliente;

    private ProfissionalResumo profissional;

    @Schema(description = "Comissao gerada pelo pedido", example = "45.00")
    private BigDecimal comissaoGerada;

    @Schema(description = "Valor total", example = "90.00")
    private BigDecimal valorTotal;

    @Schema(example = "true")
    private Boolean caixaAberto;

    @Schema(description = "Pontuação gerada para o profissional", example = "100")
    private BigDecimal pontuacaoProfissionalGerada;

    @Schema(description = "Pontuação gerada para o cliente", example = "100")
    private BigDecimal pontuacaoClienteGerada;

    private UsuarioResumo criadoPor;

    @Schema(description ="Pedido criado no horario:", example = "2024-01-25T14:30:00-03:00")
    private OffsetDateTime criadoAs;

    private UsuarioResumo alteradoPor;

    @Schema(description ="Pedido modificado no horario:", example = "2024-01-25T14:30:00-03:00")
    private OffsetDateTime modificadoAs;

    private UsuarioResumo recibidoPor;

    @Schema(description ="Pedido pago no horario:", example = "2024-01-25T14:30:00-03:00")
    private LocalDateTime dataPagamento;

    private UsuarioResumo canceladoPor;

    @Schema(description ="Pedido cancelado no horario:", example = "2024-01-25T14:30:00-03:00")
    private OffsetDateTime canceladoAs;

    private UsuarioResumo exlcuidoPor;

    @Schema(description ="Pedido excluido no horario:", example = "2024-01-25T14:30:00-03:00")
    private OffsetDateTime excluidoAs;

    @Schema(description ="Pedido começou a ser executado no horario:", example = "2024-01-25T14:30:00-03:00")
    private OffsetDateTime inicioAtendimento;

    @Schema(description ="Descricao do pedido/agendamento:", example = "Corte degrade navalhado")
    private String descricao;

    @Schema(description ="Duração programada para o atendimento", example = "00:45")
    private String duracao;

    @Schema(description ="Hora programada para terminar o atendimento", example = "2024-01-25T14:45:00-03:00")
    private OffsetDateTime fim_horario;

    @Schema(description ="Hora que o cliente chegou", example = "2024-01-25T14:45:00-03:00")
    private OffsetDateTime inicioEspera;

}
