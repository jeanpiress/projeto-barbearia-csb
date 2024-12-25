package com.jeanpiress.ProjetoBarbearia.domain.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    private LocalDate dataNascimento;
    private BigDecimal salarioFixo;
    private Integer diaPagamento;
    private boolean ativo = true;


    @Embedded
    private Endereco endereco;
}
