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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

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


    @PreAuthorize("hasAuthority('GERENTE')")
    @GetMapping("/faturamento")
    public ResponseEntity<RelatorioFaturamento> buscarFaturamentoData(@RequestParam String dataInicio,
                                                                      @RequestParam String dataFim){

        RelatorioFaturamento faturamentoPorData = relatorioService.buscarFaturamentoData(dataInicio, dataFim);
        return ResponseEntity.ok(faturamentoPorData);
    }


    @PreAuthorize("hasAuthority('GERENTE')")
    @GetMapping("/comissoes/data")
    public ResponseEntity<List<RelatorioComissaoDto>> buscarRelatorioComissao(@RequestParam(required = false) String dataInicio,
                                                                              @RequestParam(required = false) String dataFim){

        List<RelatorioComissao> relatoriosComissoes = relatorioService.buscarTodasComissoes(dataInicio, dataFim);
        return ResponseEntity.ok(relatorioComissaoAssembler.collectionToModel(relatoriosComissoes));
    }

    @PreAuthorize("hasAuthority('PROFISSIONAL')")
    @GetMapping("/comissoes/{profissionalId}")
    public ResponseEntity<RelatorioComissaoDetalhadaDto> buscarRelatorioComissaoPorProfissional(
                                                            @RequestParam String dataInicio,
                                                            @RequestParam String dataFim,
                                                            @PathVariable Long profissionalId) throws Exception {


        RelatorioComissaoDetalhada relatorioComissao = relatorioService.buscarComissaoPorProfissional(dataInicio, dataFim, profissionalId);

        return ResponseEntity.ok(relatorioComissaoDetalhadaAssembler.toModel(relatorioComissao));
    }

    @PreAuthorize("hasAuthority('RECEPCAO')")
    @GetMapping("/cliente/volta/{dias}")
    public ResponseEntity<List<ClientesRetornoDto>> clientesParaVoltar(@PathVariable Integer dias){
        List<ClientesRetorno> clientes = relatorioService.buscarClientesParaRetornar(dias);
        return ResponseEntity.ok(clientesRetornoAssembler.collectionToModel(clientes));
    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @PostMapping("/compara/mes")
    public ResponseEntity<ComparacaoMes> comparacaoFaturamentoMesFechado(@RequestParam String dataInicio,
                                                                         @RequestParam String dataFim){
        ComparacaoMes comparacaoMes = relatorioService.compararMetricasMesFechado(dataInicio, dataFim);

        return ResponseEntity.ok(comparacaoMes);
    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @PostMapping("/compara/data")
    public ResponseEntity<ComparacaoMes> comparaDataAtualComMesFornecido(@RequestParam String data){
        ComparacaoMes comparacaoDataAtual = relatorioService.compararMetricasDataAtualMesmoPeriodoMesSelecionado(LocalDate.parse(data));
        return ResponseEntity.ok(comparacaoDataAtual);
    }
}
