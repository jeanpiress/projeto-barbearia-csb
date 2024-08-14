package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalIdNome;
import com.jeanpiress.ProjetoBarbearia.domain.model.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embedded;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClienteDto {

    @Schema(description = "ID do cliente", example = "1")
    private Long id;
    @Schema(example = "João")
    private String nome;
    @Schema(example = "34999999999")
    private String celular;
    @Schema(example = "1991-12-25")
    private OffsetDateTime dataNascimento;
    @Schema(example = "2024-01-25")
    private OffsetDateTime ultimaVisita;
    private Integer pontos;
    @Schema(example = "2024-02-25")
    private OffsetDateTime previsaoRetorno;
    @Schema(example = "Degrade na maquina zero")
    private String observacao;
    @Schema(example = "30")
    private Integer diasRetorno;
    private ProfissionalIdNome profissional;

    @Embedded
    private Endereco endereco;
}
