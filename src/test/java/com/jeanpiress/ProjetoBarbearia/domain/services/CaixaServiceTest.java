package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.CaixaModel;
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
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        caixaModel = new CaixaModel(BigDecimal.valueOf(100), BigDecimal.valueOf(200), BigDecimal.valueOf(300), BigDecimal.valueOf(400),
                BigDecimal.valueOf(500), BigDecimal.valueOf(600), BigDecimal.valueOf(1000), 6, 12);

        profissional = new Profissional();
        cliente = new Cliente();

        itemPedido = new ItemPedido(1L, null, null, 2, null, null );
        itensPedido = new ArrayList<>();
        itensPedido.add(itemPedido);

        pedidoDinheiro = new Pedido(1L, OffsetDateTime.parse("2024-05-19T15:30:00-03:00"), itensPedido, StatusPagamento.PAGO,
                FormaPagamento.DINHEIRO, StatusPedido.FINALIZADO, cliente, profissional, BigDecimal.valueOf(50), BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),true, BigDecimal.valueOf(100), OffsetDateTime.now(), null, null, null, null, null, null, null, Usuario.builder().build(), OffsetDateTime.now(), null);

        pedidoPix = new Pedido(2L, OffsetDateTime.parse("2024-05-18T15:30:00-03:00"), itensPedido, StatusPagamento.PAGO,
                FormaPagamento.PIX, StatusPedido.FINALIZADO, cliente, profissional, BigDecimal.valueOf(100), BigDecimal.valueOf(200),
                BigDecimal.valueOf(200), true, BigDecimal.valueOf(200), OffsetDateTime.now(), null, null, null, null, null, null, null, Usuario.builder().build(), OffsetDateTime.now(), null);

        pedidoCredito = new Pedido(3L, OffsetDateTime.parse("2024-05-17T15:30:00-03:00"), itensPedido, StatusPagamento.PAGO,
                FormaPagamento.CREDITO, StatusPedido.FINALIZADO, cliente, profissional, BigDecimal.valueOf(150), BigDecimal.valueOf(300),
                BigDecimal.valueOf(300), true, BigDecimal.valueOf(300), OffsetDateTime.now(), null, null, null, null, null, null, null, Usuario.builder().build(), OffsetDateTime.now(), null);

        pedidoDebito = new Pedido(4L, OffsetDateTime.parse("2024-05-16T15:30:00-03:00"), itensPedido, StatusPagamento.PAGO,
                FormaPagamento.DEBITO, StatusPedido.FINALIZADO, cliente, profissional, BigDecimal.valueOf(200), BigDecimal.valueOf(400),
                BigDecimal.valueOf(400), true, BigDecimal.valueOf(400), OffsetDateTime.now(), null, null, null, null, null, null, null, Usuario.builder().build(), OffsetDateTime.now(), null);

        pedidoVoucher = new Pedido(5L, OffsetDateTime.parse("2024-05-15T15:30:00-03:00"), itensPedido, StatusPagamento.PAGO,
                FormaPagamento.VOUCHER, StatusPedido.FINALIZADO, cliente, profissional, BigDecimal.valueOf(250), BigDecimal.valueOf(500),
                BigDecimal.valueOf(500), true, BigDecimal.valueOf(500), OffsetDateTime.now(), null, null, null, null, null, null, null, Usuario.builder().build(), OffsetDateTime.now(), null);

        pedidoPontos = new Pedido(6L, OffsetDateTime.parse("2024-05-14T15:30:00-03:00"), itensPedido, StatusPagamento.PAGO,
                FormaPagamento.PONTO, StatusPedido.FINALIZADO, cliente, profissional, BigDecimal.valueOf(300), BigDecimal.valueOf(600),
                BigDecimal.valueOf(600), true, BigDecimal.valueOf(600), OffsetDateTime.now(), null, null, null, null, null, null, null, Usuario.builder().build(), OffsetDateTime.now(), null);

        pedidos = new ArrayList<>();
        pedidos.addAll(Arrays.asList(pedidoDinheiro, pedidoPix, pedidoCredito, pedidoDebito, pedidoVoucher, pedidoPontos));

    }


    @Test
    public void deveGerarCaixaDiario() {
        Mockito.when(pedidoRepository.findByPagoAndCaixaAberto()).thenReturn(pedidos);

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
        Mockito.when(pedidoRepository.findByPagoAndCaixaAberto()).thenReturn(pedidos);

        caixaService.fecharCaixa();
        for(Pedido pedido: pedidos){
            Assertions.assertFalse(pedido.isCaixaAberto());
        }

        verify(pedidoRepository).findByPagoAndCaixaAberto();
        verify(pedidoRepository).saveAll(pedidos);
        verifyNoMoreInteractions(pedidoRepository);
    }

}