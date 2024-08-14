package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProdutoIdNome;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalIdNome;
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
public class ComissaoDto {

    @Schema(description = "ID da comissao", example = "1")
    private Long id;

    @Schema(example = "Corte")
    private ProdutoIdNome produto;

    private ProfissionalIdNome profissional;

    @Schema(description = "Porcentagem paga de comissao", example = "50.00")
    private BigDecimal porcentagemComissao;

}
