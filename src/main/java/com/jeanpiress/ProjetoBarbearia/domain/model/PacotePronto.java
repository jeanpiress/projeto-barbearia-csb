package com.jeanpiress.ProjetoBarbearia.domain.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class PacotePronto {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private Integer validade;

    private boolean ativo = true;

    private BigDecimal comissaoBase;

    private BigDecimal pesoPontuacaoCliente;

    private BigDecimal pesoPontuacaoProfissional;

    @ManyToMany
    @JoinTable(name = "pacote_pronto_item", joinColumns = @JoinColumn(name = "pacote_pronto_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<ItemPacote> itensAtivos;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

}