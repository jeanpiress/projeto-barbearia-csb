package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ClientesRetornoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.RelatorioComissaoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.RelatorioComissaoDetalhadaAssembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.*;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ClienteIdNome;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalIdNome;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.DataInicioFim;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.MesAno;
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
    DataInicioFim dataInicioFim;
    MesAno mesAno;
    RelatorioFaturamento relatorioFaturamento;
    List<RelatorioComissao> relatorioComissaoList;
    List<RelatorioComissaoDto> relatorioComissaoDtoList;
    RelatorioComissaoDetalhada relatorioComissaoDetalhada;
    RelatorioComissaoDetalhadaDto relatorioComissaoDetalhadaDto;
    List<ClientesRetorno> clientesRetornoList;
    List<ClientesRetornoDto> clientesRetornoDtoList;
    ComparacaoMes comparacaoMes;
    Cliente cliente;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(relatorioController).alwaysDo(print()).build();
        cliente = new Cliente(1L, "João", "34999999999", OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), null,
                BigDecimal.ZERO, OffsetDateTime.parse("2024-11-13T00:00:00-03:00"), null, 30,
                Profissional.builder().id(1L).build(), null);

        dataInicioFim = new DataInicioFim(OffsetDateTime.parse("2024-04-01T16:00:00-03:00"),
                OffsetDateTime.parse("2024-05-01T16:00:00-03:00"));
        mesAno = new MesAno(OffsetDateTime.parse("2024-04-12T12:00:50-03:00"));
        relatorioFaturamento = new RelatorioFaturamento(BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE, 1, 1);
        relatorioComissaoList = Arrays.asList(new RelatorioComissao(Profissional.builder().id(1L).nome("Joao").build(),
                BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE, 1));
        relatorioComissaoDtoList = Arrays.asList(new RelatorioComissaoDto(ProfissionalIdNome.builder().id(1L).nome("Joao").build(),
                BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE, 1));
        relatorioComissaoDetalhada = new RelatorioComissaoDetalhada(Profissional.builder().id(1L).nome("Joao").build(),
                BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE, 1, List.of());
        relatorioComissaoDetalhadaDto = new RelatorioComissaoDetalhadaDto(ProfissionalIdNome.builder().id(1L).nome("Joao").build(),
                BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE, 1, List.of());
        clientesRetornoList = Arrays.asList(new ClientesRetorno(cliente, 1L, OffsetDateTime.parse("2024-05-01T15:00:00-03:00")));
        clientesRetornoDtoList = Arrays.asList(new ClientesRetornoDto(ClienteIdNome.builder().nome("Joao").id(1L).build(),
                1L, OffsetDateTime.parse("2024-05-01T15:00:00-03:00")));
        comparacaoMes = new ComparacaoMes(OffsetDateTime.parse("2024-05-01T15:00:00-03:00"), OffsetDateTime.parse("2024-06-01T15:00:00-03:00"),
                BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE, 0, 1,
                1, BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE);
    }

    @Test
    void deveBuscarFaturamentoData() throws Exception {
        when(relatorioService.buscarFaturamentoData(any(DataInicioFim.class))).thenReturn(relatorioFaturamento);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(post("/relatorios/faturamento-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dataInicioFim)))
                .andExpect(status().isOk())
                .andReturn();

        verify(relatorioService).buscarFaturamentoData(any(DataInicioFim.class));
        verifyNoMoreInteractions(relatorioService);
    }

    @Test
    void deveBuscarRelatorioComissao() throws Exception {
        when(relatorioService.buscarTodasComissoes(any(DataInicioFim.class))).thenReturn(relatorioComissaoList);
        when(relatorioComissaoAssembler.collectionToModel(anyList())).thenReturn(relatorioComissaoDtoList);

        mockMvc.perform(post("/relatorios/comissoes-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(dataInicioFim)))
                .andExpect(status().isOk())
                .andReturn();

        verify(relatorioService).buscarTodasComissoes(any(DataInicioFim.class));
        verify(relatorioComissaoAssembler).collectionToModel(anyList());
        verifyNoMoreInteractions(relatorioService);
    }

    @Test
    void deveBuscarRelatorioComissaoPorProfissional() throws Exception {
        when(relatorioService.buscarComissaoPorProfissional(any(DataInicioFim.class), anyLong())).thenReturn(relatorioComissaoDetalhada);
        when(relatorioComissaoDetalhadaAssembler.toModel(any(RelatorioComissaoDetalhada.class))).thenReturn(relatorioComissaoDetalhadaDto);

        mockMvc.perform(post("/relatorios/comissao/{profissionalId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(dataInicioFim)))
                .andExpect(status().isOk())
                .andReturn();

        verify(relatorioService).buscarComissaoPorProfissional(any(DataInicioFim.class), anyLong());
        verify(relatorioComissaoDetalhadaAssembler).toModel(any(RelatorioComissaoDetalhada.class));
        verifyNoMoreInteractions(relatorioService);
    }

    @Test
    void deveListarClientesParaVoltar() throws Exception {
        when(relatorioService.buscarClientesParaRetornarHoje(anyInt())).thenReturn(clientesRetornoList);
        when(clientesRetornoAssembler.collectionToModel(anyList())).thenReturn(clientesRetornoDtoList);

        mockMvc.perform(get("/relatorios/cliente-volta/{dias}", 30)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(relatorioService).buscarClientesParaRetornarHoje(anyInt());
        verify(clientesRetornoAssembler).collectionToModel(anyList());
        verifyNoMoreInteractions(relatorioService);
    }

    @Test
    void deveCompararFaturamentoMesFechado() throws Exception {
        when(relatorioService.compararMetricasMesFechado(any(DataInicioFim.class))).thenReturn(comparacaoMes);

        mockMvc.perform(post("/relatorios/compara-mes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(dataInicioFim)))
                .andExpect(status().isOk())
                .andReturn();

        verify(relatorioService).compararMetricasMesFechado(any(DataInicioFim.class));
        verifyNoMoreInteractions(relatorioService);
    }

    @Test
    void deveCompararDataAtualComMesFornecido() throws Exception {
        when(relatorioService.compararMetricasDataAtualMesmoPeriodoMesSelecionado(any(MesAno.class))).thenReturn(comparacaoMes);

        mockMvc.perform(post("/relatorios/compara-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(mesAno)))
                .andExpect(status().isOk())
                .andReturn();

        verify(relatorioService).compararMetricasDataAtualMesmoPeriodoMesSelecionado(any(MesAno.class));
        verifyNoMoreInteractions(relatorioService);
    }
}
