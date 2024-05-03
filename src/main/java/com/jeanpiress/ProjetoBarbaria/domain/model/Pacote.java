package com.jeanpiress.ProjetoBarbaria.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Pacote {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cliente cliente;

    private OffsetDateTime dataCompra;
    private Integer validade;
    private OffsetDateTime dataVencimento;
    private String nome;
    private String descricao;

    @ManyToMany
    @JoinTable(name = "pacote_item_ativo", joinColumns = @JoinColumn(name = "pacote_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<ItemPacote> itensAtivos;


    @ManyToMany
    @JoinTable(name = "pacote_item_consumido", joinColumns = @JoinColumn(name = "pacote_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<ItemPacote> itensConsumidos;
}
