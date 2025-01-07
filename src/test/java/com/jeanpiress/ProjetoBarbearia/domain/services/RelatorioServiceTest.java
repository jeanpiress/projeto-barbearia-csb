package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.*;
import com.jeanpiress.ProjetoBarbearia.core.security.CsbSecurity;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPedido;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.DataInicioFimMes;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.util.Arrays;
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

    @Mock
    ProfissionalService profissionalService;

    @Mock
    ProfissionalRepository profissionalRepository;

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    CsbSecurity security;

    Pedido pedidoPagoMesQuatro;
    Pedido pedidoPagoMesCinco;
    ItemPedido itemPedido;
    Cliente cliente;
    Produto produto;
    CaixaModel caixaMesQuatro;
    CaixaModel caixaMesCinco;
    Profissional profissional;
    FaturamentoDia faturamentoDia;
    String dataInicioString;
    String dataFimString;
    LocalDateTime dataIncio;
    LocalDateTime dataFim;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        profissional = new Profissional(1L, "João Silva", "João", "34999999999",
                null, LocalDate.parse("1991-11-13"), BigDecimal.ZERO, null, true, null);

        produto = new Produto(1L, "corte", BigDecimal.valueOf(45), true, false, null,
                false, BigDecimal.ONE, BigDecimal.ONE,null, BigDecimal.valueOf(50),
                Categoria.builder().build(), null);

        cliente = new Cliente(1L, "João", "34999999999", LocalDate.parse("2024-12-13"),
                null, BigDecimal.ZERO, LocalDate.parse("2025-01-13"), null, 30, true, profissional, null);

        itemPedido = new ItemPedido(1L, null, null, 2, null, produto);

        pedidoPagoMesQuatro = pedidoCriado(1L, "2024-04-19T15:30:00-03:00");

        pedidoPagoMesCinco = pedidoCriado(2L, "2024-05-19T15:30:00-03:00");

        faturamentoDia = new FaturamentoDia(LocalDate.now(), BigDecimal.valueOf(100), BigDecimal.valueOf(100), 1);

        caixaMesQuatro = new CaixaModel(BigDecimal.valueOf(100), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.valueOf(50), BigDecimal.valueOf(100), 1, 1, List.of(faturamentoDia));

        caixaMesCinco = new CaixaModel(BigDecimal.valueOf(150), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(150), 1, 1, List.of(faturamentoDia));

        dataInicioString = "2024-04-01T16:00:00";
        dataFimString = "2024-05-01T16:00:00";

        dataIncio = LocalDateTime.parse(dataInicioString);
        dataFim = LocalDateTime.parse(dataFimString);
    }

    @Test
    public void deveBuscarPedidosPorData(){
        when(pedidoRepository.findByDataPagamento(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(pedidoPagoMesQuatro));
        when(caixaService.gerarCaixa(List.of(pedidoPagoMesQuatro))).thenReturn(caixaMesQuatro);

        RelatorioFaturamento relatorioFaturamento = relatorioService.buscarFaturamentoData("2024-04-01","2024-05-01");

        assertEquals(relatorioFaturamento.getTotal(), BigDecimal.valueOf(100));
        assertEquals(relatorioFaturamento.getTkmLoja(), BigDecimal.valueOf(100));
        assertEquals(relatorioFaturamento.getQuantidadeProdutosVendidos(), 1);
        assertEquals(relatorioFaturamento.getVoucher(), BigDecimal.ZERO);
        assertEquals(relatorioFaturamento.getPontos(), BigDecimal.valueOf(50));

        verify(pedidoRepository).findByDataPagamento(any(LocalDateTime.class), any(LocalDateTime.class));
        verify(caixaService).gerarCaixa(List.of(pedidoPagoMesQuatro));
        verifyNoMoreInteractions(pedidoRepository, caixaService);

    }

    @Test
    public void deveCompararMetricasMesFechado(){
        DataInicioFimMes dataInicioFimMes = relatorioService.gerarDatasParaComparar("2024-04-01","2024-05-01");
        when(pedidoRepository.findByDataPagamento(dataInicioFimMes.getInicioPrimeiroMes(), dataInicioFimMes.getFimPrimeiroMes()))
                .thenReturn(List.of(pedidoPagoMesQuatro));
        when(pedidoRepository.findByDataPagamento(dataInicioFimMes.getInicioSegundoMes(), dataInicioFimMes.getFimSegundoMes()))
                .thenReturn(List.of(pedidoPagoMesCinco));
        when(caixaService.gerarCaixa(List.of(pedidoPagoMesQuatro))).thenReturn(caixaMesQuatro);
        when(caixaService.gerarCaixa(List.of(pedidoPagoMesCinco))).thenReturn(caixaMesCinco);

        ComparacaoMes comparacaoMes = relatorioService.compararMetricasMesFechado("2024-04-01","2024-05-01");

        assertEquals(comparacaoMes.getDataInicio().getMonth(), dataIncio.getMonth());
        assertEquals(comparacaoMes.getDataFim().getMonth(), dataFim.getMonth());
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
        LocalDateTime dataAtual = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDate dataFornecida = LocalDate.parse("2024-04-12");
        LocalDate dataPesquisa;
        List<Month> mesesComTrintaDias = Arrays.asList(Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER);
        List<Integer> maisDeVinteNoveDias = Arrays.asList(29, 30, 31);

        boolean mesAtualComTrintaDias = mesesComTrintaDias.contains(dataFornecida.getMonth());
        boolean hojeDia31 = dataAtual.getDayOfMonth() == 31;
        boolean hojeDiaIgualSuperior29 = maisDeVinteNoveDias.contains(dataAtual.getDayOfMonth());
        boolean mesSelecionadoFeveiro = dataFornecida.getMonth().equals(Month.FEBRUARY);

        if(mesAtualComTrintaDias && hojeDia31 && !mesSelecionadoFeveiro){
            dataPesquisa = dataFornecida.withDayOfMonth(30);

        }else if(mesSelecionadoFeveiro && hojeDiaIgualSuperior29){
            dataPesquisa = dataFornecida.withDayOfMonth(28);

        }else {
            dataPesquisa = dataFornecida.withDayOfMonth(dataAtual.getDayOfMonth());
        }

        when(pedidoRepository.findByDataPagamento(dataAtual.withDayOfMonth(1).toLocalDate().atStartOfDay(), dataAtual)).thenReturn(List.of(pedidoPagoMesCinco));
        when(pedidoRepository.findByDataPagamento(dataPesquisa.withDayOfMonth(1).atStartOfDay(), dataPesquisa.atTime(dataAtual.getHour(), dataAtual.getMinute())))
                .thenReturn(List.of(pedidoPagoMesQuatro));
        when(caixaService.gerarCaixa(List.of(pedidoPagoMesQuatro))).thenReturn(caixaMesQuatro);
        when(caixaService.gerarCaixa(List.of(pedidoPagoMesCinco))).thenReturn(caixaMesCinco);

        ComparacaoMes comparacaoMes = relatorioService.compararMetricasDataAtualMesmoPeriodoMesSelecionado(dataFornecida);

        assertNotNull(comparacaoMes);
        assertEquals(dataAtual.toLocalDate(), comparacaoMes.getDataInicio());


        verify(pedidoRepository, times(2)).findByDataPagamento(any(), any());
    }

    @Test
    public void deveBuscarComissaoPorProfissional(){
        when(pedidoRepository.findByDataPagamentoAndProfissionalId(
                any(LocalDateTime.class), any(LocalDateTime.class), eq(1L)))
                .thenReturn(List.of(pedidoPagoMesQuatro, pedidoPagoMesCinco));

        when(profissionalService.buscarPorId(1L)).thenReturn(profissional);

        when(security.temAutorizacao("GERENTE")).thenReturn(true);

        RelatorioComissaoDetalhada relatorioComissaoDetalhada = relatorioService.buscarComissaoPorProfissional("2024-04-01",
                                                                                                                 "2024-05-01", 1L);

        assertEquals(relatorioComissaoDetalhada.getProfissional(), profissional);
        assertEquals(relatorioComissaoDetalhada.getTotalComissao(), BigDecimal.valueOf(90));
        assertEquals(relatorioComissaoDetalhada.getTkm(), BigDecimal.valueOf(90));
    }

    @Test
    public void deveBuscarComissaoPorProfissionalPeriodoSemPedido(){
        when(pedidoRepository.findByDataPagamentoAndProfissionalId(any(LocalDateTime.class), any(LocalDateTime.class),
                eq(1L))).thenReturn(List.of());
        when(profissionalService.buscarPorId(1L)).thenReturn(profissional);
        when(security.temAutorizacao("GERENTE")).thenReturn(true);

        RelatorioComissaoDetalhada relatorioComissaoDetalhada = relatorioService.buscarComissaoPorProfissional("2024-04-01",
                                                                                                                 "2024-05-01", 1L);

        assertEquals(relatorioComissaoDetalhada.getProfissional(), profissional);
        assertEquals(relatorioComissaoDetalhada.getTotalComissao(), BigDecimal.ZERO);
        assertEquals(relatorioComissaoDetalhada.getTkm(), BigDecimal.ZERO);
    }

    @Test
    public void deveBuscarTodasComissoes(){
        when(pedidoRepository.findByDataPagamento(
                any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(pedidoPagoMesQuatro, pedidoPagoMesCinco));
        when(profissionalRepository.buscarProfissionaisAtivos()).thenReturn(List.of(profissional));

        List<RelatorioComissao> relatorioComissaoList= relatorioService.buscarTodasComissoes("2024-04-01", "2024-05-01");
        RelatorioComissao relatorioComissao = relatorioComissaoList.get(0);

        assertEquals(relatorioComissao.getProfissional(), profissional);
        assertEquals(relatorioComissao.getTotalComissao(), BigDecimal.valueOf(90));
        assertEquals(relatorioComissao.getTkm(), BigDecimal.valueOf(90));
    }

    @Test
    public void deveBuscarClientesParaRetornarHoje(){
        when(clienteRepository.findByClientesRetornoEmDias(any(), any())).thenReturn(List.of(cliente));

        List<ClientesRetorno> ClienteParaRetornar = relatorioService.buscarClientesParaRetornar(2);
        ClientesRetorno clienteRetorno = ClienteParaRetornar.get(0);

        assertEquals(clienteRetorno.getCliente(), cliente);
        assertEquals(clienteRetorno.getPrevisaoRetorno(), cliente.getPrevisaoRetorno());
    }

    private Pedido pedidoCriado(Long id, String horario) {
        Pedido pedido = Pedido.builder()
                .id(id)
                .horario(OffsetDateTime.parse(horario))
                .itemPedidos(List.of(itemPedido))
                .statusPagamento(StatusPagamento.PAGO)
                .formaPagamento(FormaPagamento.DINHEIRO)
                .statusPedido(StatusPedido.FINALIZADO)
                .cliente(cliente)
                .profissional(profissional)
                .comissaoGerada(BigDecimal.valueOf(45))
                .pontuacaoProfissionalGerada(BigDecimal.valueOf(90))
                .pontuacaoClienteGerada(BigDecimal.valueOf(90))
                .caixaAberto(true)
                .valorTotal(BigDecimal.valueOf(90))
                .dataPagamento(LocalDateTime.now())
                .build();

        return pedido;
    }
}