package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbearia.domain.model.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class ProfissionalInput {

    @NotBlank
    @Schema(example = "João Paulo Ferreira", required = true)
    private String nome;

    @NotBlank
    @Schema(example = "João", required = true)
    private String nomeExibicao;

    @NotBlank
    @Schema(example = "34999999999", required = true)
    private String celular;

    @Schema(example = "111.111.111-11")
    private String cpf;

    @Schema(example = "1991-11-13T00:00:00Z")
    private OffsetDateTime dataNascimento;

    @Schema(description = "Salario fixo", example = "0.00")
    private BigDecimal salarioFixo;

    @Schema(description = "Dia do pagamento", example = "5")
    private Integer diaPagamento;

    private Endereco endereco;
}
