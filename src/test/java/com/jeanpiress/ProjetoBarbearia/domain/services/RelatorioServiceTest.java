package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.CaixaModel;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.RelatorioFaturamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPedido;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.DataInicioFim;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.DataInicioFimMes;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

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

    DataInicioFim dataInicioFim;
    Pedido pedidoPagoMesQuatro;
    Pedido pedidoPagoMesCinco;
    ItemPedido itemPedido;
    Cliente cliente;
    Produto produto;
    CaixaModel caixaMesQuatro;
    CaixaModel caixaMesCinco;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        dataInicioFim = new DataInicioFim(OffsetDateTime.parse("2024-04-01T16:00:00-03:00"),
                OffsetDateTime.parse("2024-05-01T16:00:00-03:00"));

        produto = new Produto(1L, "corte", BigDecimal.valueOf(45), true, false, null,
                false, BigDecimal.ONE, BigDecimal.ONE,null, BigDecimal.valueOf(50),
                Categoria.builder().build(), null);

        cliente = new Cliente(1L, "João", "34999999999", OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), null,
                BigDecimal.ZERO, null, null, 30, Profissional.builder().build(), null);

        itemPedido = new ItemPedido(1L, null, null, 2, null, produto);

        pedidoPagoMesQuatro = new Pedido(1L, OffsetDateTime.parse("2024-04-19T15:30:00-03:00"), List.of(itemPedido),
                StatusPagamento.PAGO, FormaPagamento.DINHEIRO, StatusPedido.FINALIZADO, cliente, Profissional.builder().build(),
                BigDecimal.valueOf(75), BigDecimal.valueOf(50), BigDecimal.valueOf(100), true, BigDecimal.valueOf(100),
                OffsetDateTime.parse("2024-04-19T15:30:00-03:00"),null, null, null,
                null, null, null, null);

        pedidoPagoMesCinco = new Pedido(2L, OffsetDateTime.parse("2024-05-19T15:30:00-03:00"), List.of(itemPedido),
                StatusPagamento.PAGO, FormaPagamento.DINHEIRO, StatusPedido.FINALIZADO, cliente, Profissional.builder().build(),
                BigDecimal.valueOf(75), BigDecimal.valueOf(50), BigDecimal.valueOf(100), true, BigDecimal.valueOf(150),
                OffsetDateTime.parse("2024-04-19T15:30:00-03:00"),null, null, null,
                null, null, null, null);

        caixaMesQuatro = new CaixaModel(BigDecimal.valueOf(100), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.valueOf(50), BigDecimal.valueOf(100), 1, 1);

        caixaMesCinco = new CaixaModel(BigDecimal.valueOf(150), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(150), 1, 1);
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

}