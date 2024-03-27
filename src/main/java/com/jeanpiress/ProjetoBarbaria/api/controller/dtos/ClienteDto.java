package com.jeanpiress.ProjetoBarbaria.api.controller.dtos;

import com.jeanpiress.ProjetoBarbaria.domain.model.Endereco;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import java.time.OffsetDateTime;

@Getter
@Setter
public class ClienteDto {

    private Long id;
    private String nome;
    private String celular;
    private OffsetDateTime dataNascimento;
    private OffsetDateTime ultimaVisita;
    private Integer pontos;
    private OffsetDateTime previsaoRetorno;
    private String observacao;
    private Integer diasRetorno;

    @Embedded
    private Endereco endereco;
}
