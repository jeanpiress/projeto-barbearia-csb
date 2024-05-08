package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.api.controller.openapi.RelatorioControllerOpenApi;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.RelatorioComissaoDetalhada;
import com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao.DataJson;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.ClientesRetorno;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.RelatorioComissao;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.RelatorioFaturamento;
import com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao.MesAnoJson;
import com.jeanpiress.ProjetoBarbaria.domain.model.ComparacaoMes;
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
    public ResponseEntity<RelatorioFaturamento> buscarFaturamentoData(@RequestBody @Valid DataJson dataInicioFim){
        RelatorioFaturamento faturamentoPorData = relatorioService.buscarFaturamentoDataJson(dataInicioFim);
        return ResponseEntity.ok(faturamentoPorData);
    }


    @PostMapping("/comissoes-data")
    public ResponseEntity<List<RelatorioComissao>> buscarRelatorioComissao(@RequestBody @Valid DataJson dataInicioFim){
        List<RelatorioComissao> relatoriosComissoes = relatorioService.buscarTodasComissoes(dataInicioFim);
        return ResponseEntity.ok(relatoriosComissoes);
    }

    @PostMapping("/comissao/{profissionalId}")
    public ResponseEntity<RelatorioComissaoDetalhada> buscarRelatorioComissaoPorProfissional(@RequestBody @Valid DataJson dataInicioFim,
                                                                                             @PathVariable Long profissionalId){
        RelatorioComissaoDetalhada relatorioComissao = relatorioService.buscarComissaoPorProfissional(dataInicioFim, profissionalId);
        return ResponseEntity.ok(relatorioComissao);
    }

    @GetMapping("/cliente-volta/{dias}")
    public ResponseEntity<List<ClientesRetorno>> clientesParaVoltar(@PathVariable Integer dias){
        List<ClientesRetorno> clientes = relatorioService.buscarClientesParaRetornarHoje(dias);
        return ResponseEntity.ok(clientes);
    }

    @PostMapping("/compara-mes")
    public ResponseEntity<ComparacaoMes> comparacaoFaturamentoMesFechado(@RequestBody @Valid DataJson dataJson){
        ComparacaoMes comparacaoMes = relatorioService.compararMes(dataJson);

        return ResponseEntity.ok(comparacaoMes);
    }

    @PostMapping("/compara-data")
    public ResponseEntity<ComparacaoMes> comparaDataAtualComMesFornecido(@RequestBody @Valid MesAnoJson mesAnoJson){
        ComparacaoMes comparacaoDataAtual = relatorioService.compararPorPeriodoMes(mesAnoJson);
        return ResponseEntity.ok(comparacaoDataAtual);
    }
}
