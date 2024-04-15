package com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class DataPagamentoJson {

    @NotBlank
    private String inicio;
    @NotBlank
    private String fim;
}
