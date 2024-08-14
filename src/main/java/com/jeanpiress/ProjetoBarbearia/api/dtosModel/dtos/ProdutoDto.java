package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.PacoteProntoIdNome;
import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProdutoDto {

    @Schema(description = "ID do produto", example = "1")
    private Long id;

    @Schema(example = "Corte")
    private String nome;

    @Schema(example = "45.00")
    private BigDecimal preco;

    @Schema(example = "null")
    private Integer estoque;

    @Schema(example = "false")
    private boolean vendidoPorPonto = false;

    @Schema(example = "1.50")
    private BigDecimal pesoPontuacaoCliente;

    @Schema(example = "2.50")
    private BigDecimal pesoPontuacaoProfissional;

    @Schema(example = "null")
    private BigDecimal precoEmPontos;

    @Schema(example = "50.00")
    private BigDecimal comissaoBase;

    private Categoria categoria;

    private PacoteProntoIdNome pacotePronto;
}
