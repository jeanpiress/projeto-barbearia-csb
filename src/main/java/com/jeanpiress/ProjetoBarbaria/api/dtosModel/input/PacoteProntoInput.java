package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;


import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ItemPacoteId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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



}
