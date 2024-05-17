package com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class FormaPagamentoJson {

    @NotBlank
    @Schema(example = "2024-05-06", required = true)
    private String formaPagamento;
}
