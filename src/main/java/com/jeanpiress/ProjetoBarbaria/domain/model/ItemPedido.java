package com.jeanpiress.ProjetoBarbaria.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal precoUnitario;
    private BigDecimal precoTotal;
    private Integer quantidade;

    @ManyToMany(mappedBy = "itemPedidos")
    private List<Pedido> pedidos;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;




}
