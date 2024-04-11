package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProfissionalNome;
import com.jeanpiress.ProjetoBarbaria.domain.model.Endereco;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
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
    private ProfissionalNome profissional;

    @Embedded
    private Endereco endereco;
}
