package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.domain.model.Endereco;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class ProfissionalDto {

    @ApiModelProperty(value = "ID do profissional", example = "Corte")
    private Long id;

    @ApiModelProperty(example = "João Paulo Ferreira")
    private String nome;

    @ApiModelProperty(example = "João")
    private String nomeExibicao;

    @ApiModelProperty(example = "34999999999")
    private String celular;

    @ApiModelProperty(example = "123.456.789-10")
    private String cpf;

    @ApiModelProperty(value = "Data nascimento", example = "1991-12-25")
    private OffsetDateTime dataNascimento;

    @ApiModelProperty(example = "0.00")
    private BigDecimal salarioFixo;

    @ApiModelProperty(example = "5")
    private Integer diaPagamento;

    @ApiModelProperty(example = "true")
    private boolean ativo;

    private Endereco endereco;

}
