package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.RelatorioControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ClientesRetornoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.RelatorioComissaoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.RelatorioComissaoDetalhadaAssembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.*;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.DataInicioFim;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.MesAno;
import com.jeanpiress.ProjetoBarbearia.domain.model.ComparacaoMes;
import com.jeanpiress.ProjetoBarbearia.domain.services.RelatorioService;
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

    @Autowired
    private RelatorioComissaoDetalhadaAssembler relatorioComissaoDetalhadaAssembler;

    @Autowired
    private RelatorioComissaoAssembler relatorioComissaoAssembler;

    @Autowired
    private ClientesRetornoAssembler clientesRetornoAssembler;



    @PostMapping("/faturamento/data")
    public ResponseEntity<RelatorioFaturamento> buscarFaturamentoData(@RequestBody @Valid DataInicioFim dataInicioFim){
        RelatorioFaturamento faturamentoPorData = relatorioService.buscarFaturamentoData(dataInicioFim);
        return ResponseEntity.ok(faturamentoPorData);
    }


    @PostMapping("/comissoes/data")
    public ResponseEntity<List<RelatorioComissaoDto>> buscarRelatorioComissao(@RequestBody @Valid DataInicioFim dataInicioFim){
        List<RelatorioComissao> relatoriosComissoes = relatorioService.buscarTodasComissoes(dataInicioFim);
        return ResponseEntity.ok(relatorioComissaoAssembler.collectionToModel(relatoriosComissoes));
    }

    @PostMapping("/comissao/{profissionalId}")
    public ResponseEntity<RelatorioComissaoDetalhadaDto> buscarRelatorioComissaoPorProfissional(@RequestBody @Valid DataInicioFim dataInicioFim,
                                                                                                @PathVariable Long profissionalId){
        RelatorioComissaoDetalhada relatorioComissao = relatorioService.buscarComissaoPorProfissional(dataInicioFim, profissionalId);

        return ResponseEntity.ok(relatorioComissaoDetalhadaAssembler.toModel(relatorioComissao));
    }

    @GetMapping("/cliente/volta/{dias}")
    public ResponseEntity<List<ClientesRetornoDto>> clientesParaVoltar(@PathVariable Integer dias){
        List<ClientesRetorno> clientes = relatorioService.buscarClientesParaRetornar(dias);
        return ResponseEntity.ok(clientesRetornoAssembler.collectionToModel(clientes));
    }

    @PostMapping("/compara/mes")
    public ResponseEntity<ComparacaoMes> comparacaoFaturamentoMesFechado(@RequestBody @Valid DataInicioFim dataJson){
        ComparacaoMes comparacaoMes = relatorioService.compararMetricasMesFechado(dataJson);

        return ResponseEntity.ok(comparacaoMes);
    }

    @PostMapping("/compara/data")
    public ResponseEntity<ComparacaoMes> comparaDataAtualComMesFornecido(@RequestBody @Valid MesAno mesAnoJson){
        ComparacaoMes comparacaoDataAtual = relatorioService.compararMetricasDataAtualMesmoPeriodoMesSelecionado(mesAnoJson);
        return ResponseEntity.ok(comparacaoDataAtual);
    }
}
