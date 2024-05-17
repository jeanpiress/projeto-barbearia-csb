package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.CategoriaID;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoInput {

    @NotBlank
    @Schema(example = "Corte", required = true)
    private String nome;

    @Positive
    @NotNull
    @Schema(description = "Preco do produto", example = "45.00", required = true)
    private BigDecimal preco;

    @Schema(description = "Tem estoque", example = "false")
    private boolean temEstoque;

    @Schema(description = "Quantidade de item em estoque", example = "20")
    private Integer estoque;

    @Schema(description = "Vendido por ponto", example = "false")
    private boolean vendidoPorPonto = false;

    @Schema(description = "Peso da pontuação para o cliente", example = "1.50")
    private BigDecimal pesoPontuacaoCliente;

    @Schema(description = "Peso da pontuação para o profissional", example = "2.50")
    private BigDecimal pesoPontuacaoProfissional;

    @Schema(description = "Preço em pontos", example = "500000.00")
    private BigDecimal precoEmPontos;

    @NotNull
    @Schema(description = "Comissao padrão do produto", example = "50.00", required = true)
    private BigDecimal comissaoBase;

    @NotNull
    @Valid
    private CategoriaID categoria;
}
