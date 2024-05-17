package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;


import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.CategoriaID;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ItemPacoteId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PacoteProntoInput {

    @NotNull
    @Schema(description = "validade do pacote em dias", example = "31", required = true)
    private Integer validade;
    @NotBlank
    @Schema(example = "Cabelo e barbas", required = true)
    private String nome;
    @Schema(example = "1 corte e 4 barbas")
    private String descricao;
    @Valid
    private List<ItemPacoteId> itensAtivos;
    @Schema(example = "1.50")
    private BigDecimal pesoPontuacaoCliente;
    @Schema(example = "1.50")
    private BigDecimal pesoPontuacaoProfissional;
    @Schema(example = "5.00")
    @NotNull
    private BigDecimal comissaoBase;
    @NotNull
    @Valid
    private CategoriaID categoria;



}
