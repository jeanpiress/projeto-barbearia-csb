package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CategoriaIDNome {

    @NotNull
    private Long id;
    private String nome;
}
