package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PacoteId {

    @NotNull
    private Long id;
}
