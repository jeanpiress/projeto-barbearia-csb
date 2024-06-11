package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.PedidoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.*;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ClienteIdNome;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.PedidoResumo;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPedido;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.DataInicioFim;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.DataInicioFimMes;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.MesAno;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ClienteRepository;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PedidoRepository;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class RelatorioServiceTest {

    @InjectMocks
    RelatorioService relatorioService;

    @Mock
    PedidoRepository pedidoRepository;

    @Mock
    CaixaService caixaService;

    @Mock
    ProfissionalService profissionalService;

    @Mock
    ProfissionalRepository profissionalRepository;

    @Mock
    ClienteRepository clienteRepository;

    DataInicioFim dataInicioFim;
    Pedido pedidoPagoMesQuatro;
    Pedido pedidoPagoMesCinco;
    ItemPedido itemPedido;
    Cliente cliente;
    Produto produto;
    CaixaModel caixaMesQuatro;
    CaixaModel caixaMesCinco;
    MesAno mesAno;
    Profissional profissional;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        profissional = new Profissional(1L, "João Silva", "João", "34999999999", null, OffsetDateTime.parse("1991-11-13T00:00:00-03:00"),
                BigDecimal.ZERO, null, true, null);

        dataInicioFim = new DataInicioFim(OffsetDateTime.parse("2024-04-01T16:00:00-03:00"),
                OffsetDateTime.parse("2024-05-01T16:00:00-03:00"));

        produto = new Produto(1L, "corte", BigDecimal.valueOf(45), true, false, null,
                false, BigDecimal.ONE, BigDecimal.ONE,null, BigDecimal.valueOf(50),
                Categoria.builder().build(), null);

        cliente = new Cliente(1L, "João", "34999999999", OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), null,
                BigDecimal.ZERO, OffsetDateTime.parse("2024-11-13T00:00:00-03:00"), null, 30, Profissional.builder().build(), null);

        itemPedido = new ItemPedido(1L, null, null, 2, null, produto);

        pedidoPagoMesQuatro = new Pedido(1L, OffsetDateTime.parse("2024-04-19T15:30:00-03:00"), List.of(itemPedido),
                StatusPagamento.PAGO, FormaPagamento.DINHEIRO, StatusPedido.FINALIZADO, cliente, profissional,
                BigDecimal.valueOf(75), BigDecimal.valueOf(50), BigDecimal.valueOf(100), true, BigDecimal.valueOf(100),
                OffsetDateTime.parse("2024-04-19T15:30:00-03:00"),null, null, null,
                null, null, null, null);

        pedidoPagoMesCinco = new Pedido(2L, OffsetDateTime.parse("2024-05-19T15:30:00-03:00"), List.of(itemPedido),
                StatusPagamento.PAGO, FormaPagamento.DINHEIRO, StatusPedido.FINALIZADO, cliente, profissional,
                BigDecimal.valueOf(75), BigDecimal.valueOf(50), BigDecimal.valueOf(100), true, BigDecimal.valueOf(150),
                OffsetDateTime.parse("2024-04-19T15:30:00-03:00"),null, null, null,
                null, null, null, null);

        caixaMesQuatro = new CaixaModel(BigDecimal.valueOf(100), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.valueOf(50), BigDecimal.valueOf(100), 1, 1);

        caixaMesCinco = new CaixaModel(BigDecimal.valueOf(150), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(150), 1, 1);

        mesAno = new MesAno(OffsetDateTime.parse("2024-04-12T12:00:50-03:00"));

    }

    @Test
    public void deveBuscarPedidosPorData(){
        when(pedidoRepository.findByDataPagamento(dataInicioFim.getInicio(), dataInicioFim.getFim())).thenReturn(List.of(pedidoPagoMesQuatro));
        when(caixaService.gerarCaixa(List.of(pedidoPagoMesQuatro))).thenReturn(caixaMesQuatro);

        RelatorioFaturamento relatorioFaturamento = relatorioService.buscarFaturamentoData(dataInicioFim);

        assertEquals(relatorioFaturamento.getTotal(), BigDecimal.valueOf(100));
        assertEquals(relatorioFaturamento.getTkmLoja(), BigDecimal.valueOf(100));
        assertEquals(relatorioFaturamento.getQuantidadeProdutosVendidos(), 1);
        assertEquals(relatorioFaturamento.getVoucher(), BigDecimal.ZERO);
        assertEquals(relatorioFaturamento.getPontos(), BigDecimal.valueOf(50));

        verify(pedidoRepository).findByDataPagamento(dataInicioFim.getInicio(), dataInicioFim.getFim());
        verify(caixaService).gerarCaixa(List.of(pedidoPagoMesQuatro));
        verifyNoMoreInteractions(pedidoRepository, caixaService);

    }

    @Test
    public void deveCompararMetricasMesFechado(){
        DataInicioFimMes dataInicioFimMes = relatorioService.gerarDatasParaComparar(dataInicioFim);
        when(pedidoRepository.findByDataPagamento(dataInicioFimMes.getInicioPrimeiroMes(), dataInicioFimMes.getFimPrimeiroMes()))
                .thenReturn(List.of(pedidoPagoMesQuatro));
        when(pedidoRepository.findByDataPagamento(dataInicioFimMes.getInicioSegundoMes(), dataInicioFimMes.getFimSegundoMes()))
                .thenReturn(List.of(pedidoPagoMesCinco));
        when(caixaService.gerarCaixa(List.of(pedidoPagoMesQuatro))).thenReturn(caixaMesQuatro);
        when(caixaService.gerarCaixa(List.of(pedidoPagoMesCinco))).thenReturn(caixaMesCinco);

        ComparacaoMes comparacaoMes = relatorioService.compararMetricasMesFechado(dataInicioFim);

        assertEquals(comparacaoMes.getDataInicio().getMonth(), dataInicioFim.getInicio().getMonth());
        assertEquals(comparacaoMes.getDataFim().getMonth(), dataInicioFim.getFim().getMonth());
        assertEquals(comparacaoMes.getFaturamentoPrimeiroMes(), BigDecimal.valueOf(100));
        assertEquals(comparacaoMes.getFaturamentoSegundoMes(), BigDecimal.valueOf(150));
        assertEquals(comparacaoMes.getDiferencaFaturamento(), BigDecimal.valueOf(50));
        assertEquals(comparacaoMes.getClientesAtendidosPrimeiroMes(), 1);
        assertEquals(comparacaoMes.getClientesAtendidosSegundoMes(), 1);
        assertEquals(comparacaoMes.getDiferencaClientesAtendidos(), 0);
        assertEquals(comparacaoMes.getTkmPrimeiroMes(), BigDecimal.valueOf(100));
        assertEquals(comparacaoMes.getTkmSegundoMes(), BigDecimal.valueOf(150));
        assertEquals(comparacaoMes.getDiferencaTkm(), BigDecimal.valueOf(50));


        verify(pedidoRepository).findByDataPagamento(dataInicioFimMes.getInicioPrimeiroMes(), dataInicioFimMes.getFimPrimeiroMes());
        verify(pedidoRepository).findByDataPagamento(dataInicioFimMes.getInicioSegundoMes(), dataInicioFimMes.getFimSegundoMes());
        verify(caixaService).gerarCaixa(List.of(pedidoPagoMesQuatro));
        verify(caixaService).gerarCaixa(List.of(pedidoPagoMesCinco));
        verifyNoMoreInteractions(pedidoRepository, caixaService);

    }

    @Test
    public void deveCompararMetricasDataAtualMesmoPeriodoMesSelecionadoCaso30Dias() {
        OffsetDateTime dataAtual = OffsetDateTime.now().withSecond(0).withNano(0);
        OffsetDateTime dataFornecida = OffsetDateTime.parse("2024-04-12T12:00:50-03:00");

        when(pedidoRepository.findByDataPagamento(dataAtual.withDayOfMonth(1), dataAtual)).thenReturn(List.of(pedidoPagoMesCinco));
        when(pedidoRepository.findByDataPagamento(dataFornecida.withDayOfMonth(1), dataFornecida.withDayOfMonth(dataAtual.getDayOfMonth())))
                .thenReturn(List.of(pedidoPagoMesQuatro));
        when(caixaService.gerarCaixa(List.of(pedidoPagoMesQuatro))).thenReturn(caixaMesQuatro);
        when(caixaService.gerarCaixa(List.of(pedidoPagoMesCinco))).thenReturn(caixaMesCinco);

        ComparacaoMes comparacaoMes = relatorioService.compararMetricasDataAtualMesmoPeriodoMesSelecionado(mesAno);

        assertNotNull(comparacaoMes);
        assertEquals(dataAtual, comparacaoMes.getDataInicio());


        verify(pedidoRepository, times(2)).findByDataPagamento(any(), any());
    }

    @Test
    public void deveBuscarComissaoPorProfissional(){
        when(pedidoRepository.findByDataPagamentoAndProfissionalId(dataInicioFim.getInicio(), dataInicioFim.getFim(), 1L)).
                thenReturn(List.of(pedidoPagoMesQuatro, pedidoPagoMesCinco));
        when(profissionalService.buscarPorId(1L)).thenReturn(profissional);

        RelatorioComissaoDetalhada relatorioComissaoDetalhada = relatorioService.buscarComissaoPorProfissional(dataInicioFim, 1L);

        assertEquals(relatorioComissaoDetalhada.getProfissional(), profissional);
        assertEquals(relatorioComissaoDetalhada.getTotalComissao(), BigDecimal.valueOf(150));
        assertEquals(relatorioComissaoDetalhada.getTkm(), BigDecimal.valueOf(125));
    }

    @Test
    public void deveBuscarComissaoPorProfissionalPeriodoSemPedido(){
        when(pedidoRepository.findByDataPagamentoAndProfissionalId(dataInicioFim.getInicio(), dataInicioFim.getFim(), 1L)).
                thenReturn(List.of());
        when(profissionalService.buscarPorId(1L)).thenReturn(profissional);

        RelatorioComissaoDetalhada relatorioComissaoDetalhada = relatorioService.buscarComissaoPorProfissional(dataInicioFim, 1L);

        assertEquals(relatorioComissaoDetalhada.getProfissional(), profissional);
        assertEquals(relatorioComissaoDetalhada.getTotalComissao(), BigDecimal.ZERO);
        assertEquals(relatorioComissaoDetalhada.getTkm(), BigDecimal.ZERO);
    }

    @Test
    public void deveBuscarTodasComissoes(){
        when(pedidoRepository.findByDataPagamento(dataInicioFim.getInicio(), dataInicioFim.getFim())).
                thenReturn(List.of(pedidoPagoMesQuatro, pedidoPagoMesCinco));
        when(profissionalRepository.buscarProfissionaisAtivos()).thenReturn(Set.of(profissional));

        List<RelatorioComissao> relatorioComissaoList= relatorioService.buscarTodasComissoes(dataInicioFim);
        RelatorioComissao relatorioComissao = relatorioComissaoList.get(0);

        assertEquals(relatorioComissao.getProfissional(), profissional);
        assertEquals(relatorioComissao.getTotalComissao(), BigDecimal.valueOf(150));
        assertEquals(relatorioComissao.getTkm(), BigDecimal.valueOf(125));
    }

    @Test
    public void deveBuscarClientesParaRetornarHoje(){
        when(clienteRepository.findByClientesRetornoEmDias(any(), any())).thenReturn(List.of(cliente));

        List<ClientesRetorno> ClienteParaRetornar = relatorioService.buscarClientesParaRetornarHoje(2);
        ClientesRetorno clienteRetorno = ClienteParaRetornar.get(0);

        assertEquals(clienteRetorno.getCliente(), cliente);
        assertEquals(clienteRetorno.getPrevisaoRetorno(), cliente.getPrevisaoRetorno());
    }

}