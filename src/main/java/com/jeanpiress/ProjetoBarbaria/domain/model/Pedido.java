package com.jeanpiress.ProjetoBarbaria.domain.model;

import com.jeanpiress.ProjetoBarbaria.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.StatusPedido;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OffsetDateTime horario;

    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itemPedidos = new ArrayList<>();
    private StatusPagamento statusPagamento;
    private FormaPagamento formaPagamento;
    private StatusPedido statusPedido;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;

    private BigDecimal comissaoGerada;

    private BigDecimal pontuacaoGerada;

    private boolean caixaAberto = true;

    private BigDecimal valorTotal;

    private OffsetDateTime dataPagamento;

    public void adicionarItemPedido(ItemPedido itemPedido){
        itemPedidos.add(itemPedido);
    }

    public void removerItemPedido(ItemPedido itemPedido){
        itemPedidos.remove(itemPedido);
    }


}
