package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.api.controller.openapi.RelatorioControllerOpenApi;
import com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao.DataPagamentoJson;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.ClientesRetorno;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.RelatorioComissao;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.RelatorioFaturamento;
import com.jeanpiress.ProjetoBarbaria.domain.services.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping(path ="/relatorios", produces = MediaType.APPLICATION_JSON_VALUE)
public class RelatorioController implements RelatorioControllerOpenApi {

    @Autowired
    private RelatorioService relatorioService;



    @PostMapping("/faturamento-data")
    public ResponseEntity<RelatorioFaturamento> buscarFaturamentoData(@RequestBody @Valid DataPagamentoJson dataInicioFim){
        RelatorioFaturamento faturamentoPorData = relatorioService.buscarFaturamento(dataInicioFim);
        return ResponseEntity.ok(faturamentoPorData);
    }

    @PostMapping("/comissoes-data")
    public ResponseEntity<List<RelatorioComissao>> buscarRelatorioComissao(@RequestBody @Valid DataPagamentoJson dataInicioFim){
        List<RelatorioComissao> relatoriosComissoes = relatorioService.buscarComissoes(dataInicioFim);
        return ResponseEntity.ok(relatoriosComissoes);
    }

    @GetMapping("/cliente-volta/{dias}")
    public ResponseEntity<List<ClientesRetorno>> clientesParaVoltar(@PathVariable Integer dias){
        List<ClientesRetorno> clientes = relatorioService.buscarClientesParaRetornarHoje(dias);
        return ResponseEntity.ok(clientes);
    }

}
