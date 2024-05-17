package com.jeanpiress.ProjetoBarbearia.domain.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private BigDecimal preco;
    private boolean ativo = true;
    private boolean temEstoque;
    private Integer estoque;
    private boolean vendidoPorPonto;
    private BigDecimal pesoPontuacaoCliente;
    private BigDecimal pesoPontuacaoProfissional;
    private BigDecimal precoEmPontos;
    private BigDecimal comissaoBase;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "pacote_pronto_id")
    private PacotePronto pacotePronto;


}
