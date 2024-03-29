package com.jeanpiress.ProjetoBarbaria.api.controller.dtos;

import com.jeanpiress.ProjetoBarbaria.domain.model.Endereco;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class ProfissionalDto {

    private Long id;
    private String nome;
    private String nomeExibicao;
    private String celular;
    private String cpf;
    private OffsetDateTime dataNascimento;
    private BigDecimal salarioFixo;
    private Integer diaPagamento;
    private boolean ativo;
    private Endereco endereco;

}
