package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;


import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.CategoriaID;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ItemPacoteId;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "validade do pacote em dias", example = "31", required = true)
    private Integer validade;
    @NotBlank
    @ApiModelProperty(example = "Cabelo e barbas", required = true)
    private String nome;
    @ApiModelProperty(example = "1 corte e 4 barbas")
    private String descricao;
    @Valid
    private List<ItemPacoteId> itensAtivos;
    @ApiModelProperty(example = "1.50")
    private BigDecimal pesoPontuacaoCliente;
    @ApiModelProperty(example = "1.50")
    private BigDecimal pesoPontuacaoProfissional;
    @ApiModelProperty(example = "5.00")
    @NotNull
    private BigDecimal comissaoBase;
    @NotNull
    @Valid
    private CategoriaID categoria;



}
