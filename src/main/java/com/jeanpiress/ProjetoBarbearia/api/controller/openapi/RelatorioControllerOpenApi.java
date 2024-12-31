package com.jeanpiress.ProjetoBarbearia.api.controller.openapi;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Relatorios")
public interface RelatorioControllerOpenApi {

    @Operation(summary ="Busca o faturamento do periodo", responses = {
            @ApiResponse(responseCode = "400", description = "Data invalida", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<RelatorioFaturamento> buscarFaturamentoData(@Parameter(description = "Data inicial da pesquisa")
                                                                          String dataInicio,
                                                                      @Parameter(description = "Data final da pesquisa")
                                                                          String dataFim);

    @Operation(summary ="Busca as comissoes do periodo de todos profissionais", responses = {
            @ApiResponse(responseCode = "400", description = "Data invalida", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<List<RelatorioComissaoDto>> buscarRelatorioComissao(@Parameter(description = "Data inicial da pesquisa")
                                                                                  String dataInicio,
                                                                              @Parameter(description = "Data final da pesquisa")
                                                                                  String dataFim);

    @Operation(summary ="Busca as comissoes do periodo de um profissional expecifico", responses = {
            @ApiResponse(responseCode = "400", description = "Informações invalidas", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<RelatorioComissaoDetalhadaDto> buscarRelatorioComissaoPorProfissional(@Parameter(description = "Data inicial da pesquisa")
                                                                                                    String dataInicio,
                                                                                                @Parameter(description = "Data final da pesquisa")
                                                                                                    String dataFim,
                                                                                                @Parameter(description = "ID de um profissional", example = "1")
                                                                                                    Long profissionalId);

    @Operation(summary ="Busca os clientes que não voltaram", responses = {
            @ApiResponse(responseCode = "400", description = "Informações invalidas", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<List<ClientesRetornoDto>> clientesParaVoltar(@Parameter(description = "Numero de dias que deseja pesquisar a partir da data atual", example = "1")
                                                                        Integer dias);

}


