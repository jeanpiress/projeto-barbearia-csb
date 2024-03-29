package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbaria.domain.model.Endereco;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
public class ClienteInput {

    @NotBlank
    private String nome;
    @NotBlank
    private String celular;
    private OffsetDateTime dataNascimento;
    private String observacao;
    @NotNull
    private Integer diasRetorno;
    private Endereco endereco;
}
