package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CategoriaInput {

    @Schema(example = "Cabelo", required = true)
    @NotNull
    private String nome;
}
