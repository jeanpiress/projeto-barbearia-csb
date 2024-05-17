package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class PacoteDto {

    @Schema(description = "ID do pacote", example = "1")
    private Long id;
    private ClienteIdNome cliente;
    @Schema(description = "nome do pacote", example = "corte e barbas")
    private String nome;
    @Schema(description = "descricao do que vem no pacote", example = "1 corte e 4 barbas")
    private String descricao;
    @Schema(example = "2024-04-01T00:00:00Z")
    private OffsetDateTime dataCompra;
    @Schema(example = "2024-04-01T00:00:00Z")
    private OffsetDateTime dataVencimento;
    private List<ItemPacoteResumo> itensAtivos;
    private List<ItemPacoteResumo> itensConsumidos;
    private List<ItemPacoteResumo> itensExpirados;


}
