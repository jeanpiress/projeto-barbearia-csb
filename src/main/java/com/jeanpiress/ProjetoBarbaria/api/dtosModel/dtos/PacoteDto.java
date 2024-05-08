package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class PacoteDto {

    @ApiModelProperty(value = "ID do pacote", example = "1")
    private Long id;
    private ClienteIdNome cliente;
    @ApiModelProperty(value = "nome do pacote", example = "corte e barbas")
    private String nome;
    @ApiModelProperty(value = "descricao do que vem no pacote", example = "1 corte e 4 barbas")
    private String descricao;
    @ApiModelProperty(example = "2024-04-01T00:00:00Z")
    private OffsetDateTime dataCompra;
    @ApiModelProperty(example = "2024-04-01T00:00:00Z")
    private OffsetDateTime dataVencimento;
    private List<ItemPacoteResumo> itensAtivos;
    private List<ItemPacoteResumo> itensConsumidos;
    private List<ItemPacoteResumo> itensExpirados;


}
