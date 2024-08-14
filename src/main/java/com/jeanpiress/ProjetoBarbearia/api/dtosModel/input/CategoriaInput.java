package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CategoriaInput {

    @Schema(example = "Cabelo", required = true)
    @NotNull
    private String nome;
}
