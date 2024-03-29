package com.jeanpiress.ProjetoBarbaria.api.controller.dtos.input;

import com.jeanpiress.ProjetoBarbaria.domain.model.Endereco;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class ProfissionalInput {

    @NotBlank
    private String nome;
    @NotBlank
    private String nomeExibicao;
    @NotBlank
    private String celular;
    private String cpf;
    private OffsetDateTime dataNascimento;
    private BigDecimal salarioFixo;
    private Integer diaPagamento;
    private Endereco endereco;
}
