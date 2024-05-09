package com.jeanpiress.ProjetoBarbaria.domain.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String celular;
    private OffsetDateTime dataNascimento;
    private OffsetDateTime ultimaVisita;
    private BigDecimal pontos;
    private OffsetDateTime previsaoRetorno;
    private String observacao;
    private Integer diasRetorno;

    @ManyToOne
    @JoinColumn(name = "ultimo_profissional_id")
    private Profissional ultimoProfissional;

    @Embedded
    private Endereco endereco;
}
