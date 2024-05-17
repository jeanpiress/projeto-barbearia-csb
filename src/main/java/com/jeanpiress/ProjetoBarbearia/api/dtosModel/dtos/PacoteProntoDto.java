package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ItemPacoteResumo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PacoteProntoDto {

    @Schema(description = "ID do pacote pronto", example = "1")
    private Long id;
    @Schema(description = "Quantidade de dias que o pacote é valido", example = "31")
    private Integer validade;
    @Schema(description = "Nome do pacote", example = "Corte e barbas")
    private String nome;
    @Schema(description = "descricao do que vem no pacote", example = "1 corte e 4 barbas")
    private String descricao;
    private List<ItemPacoteResumo> itensAtivos;



}
