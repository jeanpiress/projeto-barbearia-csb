package com.jeanpiress.ProjetoBarbearia.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class Endereco {

    @ApiModelProperty(example = "23.456.789")
    @Column(name = "endereco_cep")
    private String cep;

    @ApiModelProperty(example = "Rua Canção")
    @Column(name = "endereco_logradouro")
    private String logradouro;

    @ApiModelProperty(example = "123")
    @Column(name = "endereco_numero")
    private String numero;

    @ApiModelProperty(example = "ape 456")
    @Column(name = "endereco_complemento")
    private String complemento;

    @ApiModelProperty(example = "Morumbi")
    @Column(name = "endereco_bairro")
    private String bairro;
}
