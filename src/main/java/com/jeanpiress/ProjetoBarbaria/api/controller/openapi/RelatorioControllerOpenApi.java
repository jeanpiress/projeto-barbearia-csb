package com.jeanpiress.ProjetoBarbaria.api.controller.openapi;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.ClientesRetorno;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.RelatorioComissao;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.RelatorioFaturamento;
import com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao.DataJson;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Relatorios")
public interface RelatorioControllerOpenApi {

    @ApiOperation("Busca o faturamento do periodo")
    public ResponseEntity<RelatorioFaturamento> buscarFaturamentoData(@ApiParam(name = "Corpo", value = "Representação de data inicio e fim")
                                                                      DataJson dataInicioFim);

    @ApiOperation("Busca as comissoes do periodo")
    public ResponseEntity<List<RelatorioComissao>> buscarRelatorioComissao(@ApiParam(name = "Corpo", value = "Representação de data inicio e fim")
                                                                           DataJson dataInicioFim);

    @ApiOperation("Busca os clientes que não voltaram")
    public ResponseEntity<List<ClientesRetorno>> clientesParaVoltar(@ApiParam(value = "Numero de dias que deseja pesquisar a partir da data atual", example = "1")
                                                                        Integer dias);

}


