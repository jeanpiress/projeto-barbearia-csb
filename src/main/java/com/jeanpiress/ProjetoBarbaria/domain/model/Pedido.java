package com.jeanpiress.ProjetoBarbaria.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.StatusPedido;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Data
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

    @JsonIgnore
    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itemPedidos;

    private StatusPagamento statusPagamento;
    private FormaPagamento formaPagamento;
    private StatusPedido statusPedido;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;


}
