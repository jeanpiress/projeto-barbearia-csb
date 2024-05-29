package com.jeanpiress.ProjetoBarbearia.api.controller.openapi;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.ClientesRetorno;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.RelatorioComissao;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.RelatorioFaturamento;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.DataInicioFim;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Relatorios")
public interface RelatorioControllerOpenApi {

    @Operation(summary ="Busca o faturamento do periodo")
    public ResponseEntity<RelatorioFaturamento> buscarFaturamentoData(@Parameter(description = "Representação de data inicio e fim")
                                                                      DataInicioFim dataInicioFim);

    @Operation(summary ="Busca as comissoes do periodo")
    public ResponseEntity<List<RelatorioComissao>> buscarRelatorioComissao(@Parameter(description = "Representação de data inicio e fim")
                                                                               DataInicioFim dataInicioFim);

    @Operation(summary ="Busca os clientes que não voltaram")
    public ResponseEntity<List<ClientesRetorno>> clientesParaVoltar(@Parameter(description = "Numero de dias que deseja pesquisar a partir da data atual", example = "1")
                                                                        Integer dias);

}


