package com.jeanpiress.ProjetoBarbaria.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemPacote {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_pedido_id")
    private ItemPedido itemPedido;

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;

    private OffsetDateTime dataConsumo;

}
