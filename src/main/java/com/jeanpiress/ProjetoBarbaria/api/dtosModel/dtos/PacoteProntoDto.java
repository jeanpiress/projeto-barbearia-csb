package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ItemPacoteResumo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PacoteProntoDto {

    private Long id;
    private Integer validade;
    private String nome;
    private String descricao;
    private List<ItemPacoteResumo> itensAtivos;



}
