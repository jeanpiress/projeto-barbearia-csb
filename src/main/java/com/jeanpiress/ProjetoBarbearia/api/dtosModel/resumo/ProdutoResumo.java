package com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProdutoResumo {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "Corte")
    private String nome;
    @Schema(example = "45.00")
    private BigDecimal preco;
    @Schema(example = "1500")
    private BigDecimal precoEmPontos;
    private CategoriaIDNome categoria;
}
