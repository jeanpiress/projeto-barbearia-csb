package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ItemPacoteResumo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PacoteProntoDto {

    @ApiModelProperty(value = "ID do pacote pronto", example = "1")
    private Long id;
    @ApiModelProperty(value = "Quantidade de dias que o pacote é valido", example = "31")
    private Integer validade;
    @ApiModelProperty(value = "Nome do pacote", example = "Corte e barbas")
    private String nome;
    @ApiModelProperty(value = "descricao do que vem no pacote", example = "1 corte e 4 barbas")
    private String descricao;
    private List<ItemPacoteResumo> itensAtivos;



}
