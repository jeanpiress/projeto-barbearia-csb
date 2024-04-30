package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbaria.domain.model.Endereco;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
public class ClienteInput {


    @NotBlank
    @ApiModelProperty(example = "João", required = true)
    private String nome;

    @NotBlank
    @ApiModelProperty(example = "34999999999", required = true)
    private String celular;

    @ApiModelProperty(value = "Data de nascimento do cliente", example = "1991-12-25")
    private OffsetDateTime dataNascimento;

    @ApiModelProperty(example = "Degrade até a maquina zero")
    private String observacao;

    @NotNull
    @ApiModelProperty(value = "A quatidade de dias que o cliente cosutma voltar", example = "30", required = true)
    private Integer diasRetorno;

    private Endereco endereco;
}
