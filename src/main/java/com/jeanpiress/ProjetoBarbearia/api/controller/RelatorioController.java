package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.RelatorioControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ClientesRetornoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.RelatorioComissaoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.RelatorioComissaoDetalhadaAssembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.*;
import com.jeanpiress.ProjetoBarbearia.domain.model.ComparacaoMes;
import com.jeanpiress.ProjetoBarbearia.domain.services.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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



    @GetMapping("/faturamento")
    public ResponseEntity<RelatorioFaturamento> buscarFaturamentoData(@RequestParam String dataInicio,
                                                                      @RequestParam String dataFim){

        RelatorioFaturamento faturamentoPorData = relatorioService.buscarFaturamentoData(dataInicio, dataFim);
        return ResponseEntity.ok(faturamentoPorData);
    }


    @PostMapping("/comissoes/data")
    public ResponseEntity<List<RelatorioComissaoDto>> buscarRelatorioComissao(@RequestParam String dataInicio,
                                                                              @RequestParam String dataFim){
        List<RelatorioComissao> relatoriosComissoes = relatorioService.buscarTodasComissoes(dataInicio, dataFim);
        return ResponseEntity.ok(relatorioComissaoAssembler.collectionToModel(relatoriosComissoes));
    }

    @GetMapping("/comissoes/{profissionalId}")
    public ResponseEntity<RelatorioComissaoDetalhadaDto> buscarRelatorioComissaoPorProfissional(
                                                            @RequestParam String dataInicio,
                                                            @RequestParam String dataFim,
                                                            @PathVariable Long profissionalId){


        RelatorioComissaoDetalhada relatorioComissao = relatorioService.buscarComissaoPorProfissional(dataInicio, dataFim, profissionalId);

        return ResponseEntity.ok(relatorioComissaoDetalhadaAssembler.toModel(relatorioComissao));
    }

    @GetMapping("/cliente/volta/{dias}")
    public ResponseEntity<List<ClientesRetornoDto>> clientesParaVoltar(@PathVariable Integer dias){
        List<ClientesRetorno> clientes = relatorioService.buscarClientesParaRetornar(dias);
        return ResponseEntity.ok(clientesRetornoAssembler.collectionToModel(clientes));
    }

    @PostMapping("/compara/mes")
    public ResponseEntity<ComparacaoMes> comparacaoFaturamentoMesFechado(@RequestParam String dataInicio,
                                                                         @RequestParam String dataFim){
        ComparacaoMes comparacaoMes = relatorioService.compararMetricasMesFechado(dataInicio, dataFim);

        return ResponseEntity.ok(comparacaoMes);
    }

    @PostMapping("/compara/data")
    public ResponseEntity<ComparacaoMes> comparaDataAtualComMesFornecido(@RequestParam String data){
        ComparacaoMes comparacaoDataAtual = relatorioService.compararMetricasDataAtualMesmoPeriodoMesSelecionado(LocalDate.parse(data));
        return ResponseEntity.ok(comparacaoDataAtual);
    }
}
