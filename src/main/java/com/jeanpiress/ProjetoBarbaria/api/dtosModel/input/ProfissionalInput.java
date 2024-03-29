package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbaria.domain.model.Endereco;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
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
