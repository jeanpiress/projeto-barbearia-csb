package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProfissionalIdNome;
import com.jeanpiress.ProjetoBarbaria.domain.model.Endereco;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import java.time.OffsetDateTime;


@Getter
@Setter
public class ClienteDto {

    @ApiModelProperty(value = "ID do cliente", example = "1")
    private Long id;
    @ApiModelProperty(example = "João")
    private String nome;
    @ApiModelProperty(example = "34999999999")
    private String celular;
    @ApiModelProperty(example = "1991-12-25")
    private OffsetDateTime dataNascimento;
    @ApiModelProperty(example = "2024-01-25")
    private OffsetDateTime ultimaVisita;
    private Integer pontos;
    @ApiModelProperty(example = "2024-02-25")
    private OffsetDateTime previsaoRetorno;
    @ApiModelProperty(example = "Degrade na maquina zero")
    private String observacao;
    @ApiModelProperty(example = "30")
    private Integer diasRetorno;
    private ProfissionalIdNome profissional;

    @Embedded
    private Endereco endereco;
}
