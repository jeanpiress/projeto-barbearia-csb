package com.jeanpiress.ProjetoBarbearia.domain.model;

import com.jeanpiress.ProjetoBarbearia.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPedido;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pedido {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OffsetDateTime horario;

    @ManyToMany
    @JoinTable(name = "pedido_item_pedido", joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "item_pedido_id"))
    private List<ItemPedido> itemPedidos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;
    private FormaPagamento formaPagamento;
    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;

    private BigDecimal comissaoGerada;

    private BigDecimal pontuacaoProfissionalGerada;

    private BigDecimal pontuacaoClienteGerada;

    private boolean caixaAberto = true;

    private BigDecimal valorTotal;

    private LocalDateTime dataPagamento;

    @ManyToOne
    @JoinColumn(name = "criado_por")
    private Usuario criadoPor;

    @ManyToOne
    @JoinColumn(name = "alterado_por")
    private Usuario alteradoPor;

    @ManyToOne
    @JoinColumn(name = "recebido_por")
    private Usuario recibidoPor;

    @Column(name = "criado_as")
    private OffsetDateTime criadoAs;

    @Column(name = "modificado_as")
    private OffsetDateTime modificadoAs;

    @ManyToOne
    @JoinColumn(name = "cancelado_por")
    private Usuario canceladoPor;

    @Column(name = "cancelado_as")
    private OffsetDateTime canceladoAs;

    @ManyToOne
    @JoinColumn(name = "excluido_por")
    private Usuario excluidoPor;

    @Column(name = "excluido_as")
    private OffsetDateTime excluidoAs;

    @Column(name = "inicio_atendimento")
    private OffsetDateTime inicioAtendimento;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "duracao")
    private String duracao;

    @Column(name = "fim_horario")
    private OffsetDateTime fimHorario;

    @Column(name = "is_agendamento")
    private Boolean isAgendamento;

    @Column(name = "inicio_espera")
    private OffsetDateTime inicioEspera;
}
