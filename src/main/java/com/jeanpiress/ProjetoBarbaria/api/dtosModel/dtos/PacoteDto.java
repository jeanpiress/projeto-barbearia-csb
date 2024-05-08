package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class PacoteDto {

    private Long id;
    private ClienteIdNome cliente;
    private String nome;
    private String descricao;
    private OffsetDateTime dataCompra;
    private OffsetDateTime dataVencimento;
    private List<ItemPacoteResumo> itensAtivos;
    private List<ItemPacoteResumo> itensConsumidos;
    private List<ItemPacoteResumo> itensExpirados;


}
