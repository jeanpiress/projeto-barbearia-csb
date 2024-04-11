package com.jeanpiress.ProjetoBarbaria.domain.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Profissional {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String nomeExibicao;
    private String celular;
    private String cpf;
    private OffsetDateTime dataNascimento;
    private BigDecimal salarioFixo;
    private Integer diaPagamento;
    private boolean ativo;


    @Embedded
    private Endereco endereco;
}
