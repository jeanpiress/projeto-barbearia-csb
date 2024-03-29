package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CategoriaInput {

    @NotNull
    private String nome;
}
