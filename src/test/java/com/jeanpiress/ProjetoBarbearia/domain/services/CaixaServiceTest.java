package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.CaixaModel;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.FaturamentoDia;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPedido;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PedidoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
class CaixaServiceTest {

    @InjectMocks
    private CaixaService caixaService;

    @Mock
    private PedidoRepository pedidoRepository;

    CaixaModel caixaModel;
    Pedido pedidoDinheiro;
    Pedido pedidoPix;
    Pedido pedidoCredito;
    Pedido pedidoDebito;
    Pedido pedidoVoucher;
    Pedido pedidoPontos;
    List<Pedido> pedidos;
    ItemPedido itemPedido;
    List<ItemPedido> itensPedido;
    Cliente cliente;
    Profissional profissional;
    FaturamentoDia faturamentoDia;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        faturamentoDia = new FaturamentoDia(LocalDate.now(), BigDecimal.valueOf(360), BigDecimal.valueOf(90), 6);

        caixaModel = new CaixaModel(BigDecimal.valueOf(90), BigDecimal.valueOf(90), BigDecimal.valueOf(90), BigDecimal.valueOf(90),
                BigDecimal.valueOf(90), BigDecimal.valueOf(90), BigDecimal.valueOf(360), 6, 12, List.of(faturamentoDia));

        profissional = new Profissional();
        cliente = new Cliente();

        itemPedido = new ItemPedido(1L, null, null, 2, null, null );
        itensPedido = new ArrayList<>();
        itensPedido.add(itemPedido);

        pedidoDinheiro = pedidoCriado(1L, FormaPagamento.DINHEIRO, 1L);
        pedidoPix = pedidoCriado(2L, FormaPagamento.PIX, 2L);
        pedidoCredito = pedidoCriado(3L, FormaPagamento.CREDITO, 3L);
        pedidoDebito = pedidoCriado(4L, FormaPagamento.DEBITO, 4L);
        pedidoVoucher = pedidoCriado(5L, FormaPagamento.VOUCHER, 5L);
        pedidoPontos = pedidoCriado(6L, FormaPagamento.PONTO, 6L);

        pedidos = new ArrayList<>();
        pedidos.addAll(Arrays.asList(pedidoDinheiro, pedidoPix, pedidoCredito, pedidoDebito, pedidoVoucher, pedidoPontos));

    }


    @Test
    public void deveGerarCaixaDiario() {
        Mockito.when(pedidoRepository.findByEqualStatusAndIsCaixaAberto(true, StatusPagamento.PAGO)).thenReturn(pedidos);

        CaixaModel caixaCalculado = caixaService.gerarCaixaDiario();

        assertAll("Verificando os valores do caixa diário",
                () -> assertEquals(caixaModel.getDinheiro(), caixaCalculado.getDinheiro(), "Dinheiro"),
                () -> assertEquals(caixaModel.getPix(), caixaCalculado.getPix(), "Pix"),
                () -> assertEquals(caixaModel.getCredito(), caixaCalculado.getCredito(), "Crédito"),
                () -> assertEquals(caixaModel.getDebito(), caixaCalculado.getDebito(), "Débito"),
                () -> assertEquals(caixaModel.getVoucher(), caixaCalculado.getVoucher(), "Voucher"),
                () -> assertEquals(caixaModel.getPontos(), caixaCalculado.getPontos(), "Pontos"),
                () -> assertEquals(caixaModel.getTotal(), caixaCalculado.getTotal(), "Total"),
                () -> assertEquals(caixaModel.getClientesAtendidos(), caixaCalculado.getClientesAtendidos(), "Clientes Atendidos"),
                () -> assertEquals(caixaModel.getQuantidadeProdutosVendidos(), caixaCalculado.getQuantidadeProdutosVendidos(), "Quantidade de Produtos Vendidos")
        );

    }

    @Test
    public void deveFecharCaixa() {
        Mockito.when(pedidoRepository.findByEqualStatusAndIsCaixaAberto(true, StatusPagamento.PAGO)).thenReturn(pedidos);

        caixaService.fecharCaixa();
        for(Pedido pedido: pedidos){
            Assertions.assertFalse(pedido.isCaixaAberto());
        }

        verify(pedidoRepository).findByEqualStatusAndIsCaixaAberto(true, StatusPagamento.PAGO);
        verify(pedidoRepository).saveAll(pedidos);
        verifyNoMoreInteractions(pedidoRepository);
    }

    private Pedido pedidoCriado(Long id, FormaPagamento formaPagamento, Long clienteId) {
        Pedido pedido = Pedido.builder()
                .id(id)
                .horario(OffsetDateTime.parse("2024-05-19T15:30:00-03:00"))
                .itemPedidos(List.of(itemPedido))
                .statusPagamento(StatusPagamento.PAGO)
                .formaPagamento(formaPagamento)
                .statusPedido(StatusPedido.FINALIZADO)
                .cliente(Cliente.builder().id(clienteId).build())
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