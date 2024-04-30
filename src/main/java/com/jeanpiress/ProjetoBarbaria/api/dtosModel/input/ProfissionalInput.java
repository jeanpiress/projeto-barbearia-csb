package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbaria.domain.model.Endereco;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class ProfissionalInput {

    @NotBlank
    @ApiModelProperty(example = "João Paulo Ferreira", required = true)
    private String nome;

    @NotBlank
    @ApiModelProperty(example = "João", required = true)
    private String nomeExibicao;

    @NotBlank
    @ApiModelProperty(example = "34999999999", required = true)
    private String celular;

    @ApiModelProperty(example = "111.111.111-11")
    private String cpf;

    @ApiModelProperty(example = "1991-12-25")
    private OffsetDateTime dataNascimento;

    @ApiModelProperty(value = "Salario fixo", example = "0.00")
    private BigDecimal salarioFixo;

    @ApiModelProperty(value = "Dia do pagamento", example = "5")
    private Integer diaPagamento;

    private Endereco endereco;
}
