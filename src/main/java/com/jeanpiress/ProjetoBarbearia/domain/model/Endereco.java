package com.jeanpiress.ProjetoBarbearia.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Endereco {

    @Schema(example = "23456789")
    @Column(name = "endereco_cep")
    private String cep;

    @Schema(example = "Rua Canção")
    @Column(name = "endereco_logradouro")
    private String logradouro;

    @Schema(example = "123")
    @Column(name = "endereco_numero")
    private String numero;

    @Schema(example = "ape 456")
    @Column(name = "endereco_complemento")
    private String complemento;

    @Schema(example = "Morumbi")
    @Column(name = "endereco_bairro")
    private String bairro;

    @Schema(example = "Uberalandia")
    @Column(name = "endereco_cidade")
    private String cidade;

    @Schema(example = "Minas Gerais")
    @Column(name = "endereco_estado")
    private String estado;
}
