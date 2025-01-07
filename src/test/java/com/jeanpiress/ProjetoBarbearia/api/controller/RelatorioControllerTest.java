package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ClientesRetornoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.RelatorioComissaoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.RelatorioComissaoDetalhadaAssembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.*;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ClienteResumo;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalIdNome;
import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbearia.domain.model.ComparacaoMes;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbearia.domain.services.RelatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class RelatorioControllerTest {

    @InjectMocks
    RelatorioController relatorioController;

    @Mock
    RelatorioService relatorioService;

    @Mock
    RelatorioComissaoDetalhadaAssembler relatorioComissaoDetalhadaAssembler;

    @Mock
    RelatorioComissaoAssembler relatorioComissaoAssembler;

    @Mock
    ClientesRetornoAssembler clientesRetornoAssembler;

    MockMvc mockMvc;
    RelatorioFaturamento relatorioFaturamento;
    List<RelatorioComissao> relatorioComissaoList;
    List<RelatorioComissaoDto> relatorioComissaoDtoList;
    RelatorioComissaoDetalhada relatorioComissaoDetalhada;
    RelatorioComissaoDetalhadaDto relatorioComissaoDetalhadaDto;
    List<ClientesRetorno> clientesRetornoList;
    List<ClientesRetornoDto> clientesRetornoDtoList;
    ComparacaoMes comparacaoMes;
    Cliente cliente;
    FaturamentoDia faturamentoDia;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(relatorioController).alwaysDo(print()).build();

        faturamentoDia = new FaturamentoDia(LocalDate.now(), BigDecimal.valueOf(100), BigDecimal.valueOf(100), 1);

        cliente = new Cliente(1L, "João", "34999999999", LocalDate.parse("1991-11-13"),
                null, BigDecimal.ZERO, null, null, 30, true,
                Profissional.builder().build(), null);

        relatorioFaturamento = new RelatorioFaturamento(BigDecimal.valueOf(100.00),BigDecimal.valueOf(200.00),BigDecimal.valueOf(300.00),
                BigDecimal.valueOf(400.00),BigDecimal.valueOf(500.00),BigDecimal.valueOf(600.00),BigDecimal.valueOf(1000.00),
                BigDecimal.valueOf(200.00),5,10,List.of(faturamentoDia));

        relatorioComissaoList = Arrays.asList(new RelatorioComissao(Profissional.builder().id(1L).nome("Joao").build(),
                BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE, 1));

        relatorioComissaoDtoList = Arrays.asList(new RelatorioComissaoDto(ProfissionalIdNome.builder().id(1L).nome("Joao").build(),
                BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE, 1));

        relatorioComissaoDetalhada = new RelatorioComissaoDetalhada(Profissional.builder().id(1L).nome("Joao").build(),
                BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE, 1, List.of());

        relatorioComissaoDetalhadaDto = new RelatorioComissaoDetalhadaDto(ProfissionalIdNome.builder().id(1L).nome("Joao").build(),
                BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE, 1, List.of());

        clientesRetornoList = Arrays.asList(new ClientesRetorno(cliente, 1L, LocalDate.parse("2024-05-01")));

        clientesRetornoDtoList = Arrays.asList(new ClientesRetornoDto(ClienteResumo.builder().nome("Joao").id(1L).build(),
                1L, OffsetDateTime.parse("2024-05-01T15:00:00-03:00")));

        comparacaoMes = new ComparacaoMes(LocalDate.parse("2024-05-01"), LocalDate.parse("2024-06-01"),
                BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE, 0, 1,
                1, BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE);
    }

    @Test
    void deveBuscarFaturamentoData() throws Exception {
        when(relatorioService.buscarFaturamentoData(anyString(), anyString())).thenReturn(relatorioFaturamento);

        mockMvc.perform(get("/relatorios/faturamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("dataInicio", "2025-01-01")
                        .param("dataFim", "2025-01-31"))
                .andExpect(status().isOk())
                .andReturn();

        verify(relatorioService).buscarFaturamentoData(anyString(), anyString());
        verifyNoMoreInteractions(relatorioService);
    }

    @Test
    void deveBuscarRelatorioComissao() throws Exception {
        when(relatorioService.buscarTodasComissoes(anyString(), anyString())).thenReturn(relatorioComissaoList);
        when(relatorioComissaoAssembler.collectionToModel(anyList())).thenReturn(relatorioComissaoDtoList);

        mockMvc.perform(get("/relatorios/comissoes/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("dataInicio", "2025-01-01")
                        .param("dataFim", "2025-01-31"))
                .andExpect(status().isOk())
                .andReturn();

        verify(relatorioService).buscarTodasComissoes(anyString(), anyString());
        verify(relatorioComissaoAssembler).collectionToModel(anyList());
        verifyNoMoreInteractions(relatorioService);
    }

    @Test
    void deveBuscarRelatorioComissaoPorProfissional() throws Exception {
        when(relatorioService.buscarComissaoPorProfissional(anyString(), anyString(), anyLong())).thenReturn(relatorioComissaoDetalhada);
        when(relatorioComissaoDetalhadaAssembler.toModel(any(RelatorioComissaoDetalhada.class))).thenReturn(relatorioComissaoDetalhadaDto);

        mockMvc.perform(get("/relatorios/comissoes/{profissionalId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("dataInicio", "2025-01-01")
                        .param("dataFim", "2025-01-31"))
                .andExpect(status().isOk())
                .andReturn();

        verify(relatorioService).buscarComissaoPorProfissional(anyString(), anyString(), anyLong());
        verify(relatorioComissaoDetalhadaAssembler).toModel(any(RelatorioComissaoDetalhada.class));
        verifyNoMoreInteractions(relatorioService);
    }

    @Test
    void deveCompararFaturamentoMesFechado() throws Exception {
        when(relatorioService.compararMetricasMesFechado(anyString(), anyString())).thenReturn(comparacaoMes);

        mockMvc.perform(post("/relatorios/compara/mes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("dataInicio", "2024-12-01")
                        .param("dataFim", "2024-12-31"))
                .andExpect(status().isOk())
                .andReturn();

        verify(relatorioService).compararMetricasMesFechado(anyString(), anyString());
        verifyNoMoreInteractions(relatorioService);
    }

    @Test
    void deveCompararDataAtualComMesFornecido() throws Exception {
        when(relatorioService.compararMetricasDataAtualMesmoPeriodoMesSelecionado(any(LocalDate.class))).thenReturn(comparacaoMes);

        mockMvc.perform(post("/relatorios/compara/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("data", "2025-01-01"))
                .andExpect(status().isOk())
                .andReturn();

        verify(relatorioService).compararMetricasDataAtualMesmoPeriodoMesSelecionado(any(LocalDate.class));
        verifyNoMoreInteractions(relatorioService);
    }


}
