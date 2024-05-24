package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPedido;
import com.jeanpiress.ProjetoBarbearia.domain.eventos.PacoteRealizadoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ItemPedidoJaAdicionadoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.PedidoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PedidoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class PedidoServiceTest {

    @Spy
    @InjectMocks
    PedidoService pedidoService;

    @Mock
    PedidoRepository pedidoRepository;

    @Mock
    ComissaoService comissaoService;

    @Mock
    PacoteService pacoteService;

    @Mock
    ClienteService clienteService;

    @Mock
    ProfissionalService profissionalService;

    @Mock
    ItemPedidoService itemPedidoService;

    Pedido pedidoNovo;
    Pedido pedidoPago;
    Pedido pedidoSemItemPedido;
    Pedido pedidoAguardandoPg;
    Cliente cliente;
    Produto produto;
    Profissional profissional;
    ItemPedido itemPedido;
    ItemPacote itemPacoteConsumidoRecente;
    ItemPacote itemPacoteConsumidoAntigo;
    Pacote pacoteItemConsumido;
    PacoteRealizadoEvento pacoteRealizadoEvento;
    List<ItemPacote> itensPacoteConsumidos = new ArrayList<>();
    Comissao comissao;
    List<ItemPedido> listaVazia = new ArrayList<>();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente(1L, "João", "34999999999", OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), null,
                BigDecimal.ZERO, null, null, 30, Profissional.builder().build(), null);

        profissional = new Profissional(1L, "João Silva", "João", "34999999999", null, OffsetDateTime.parse("1991-11-13T00:00:00-03:00"),
                BigDecimal.ZERO, null, true, null);

        produto = new Produto(1L, "corte", BigDecimal.valueOf(45), true, false, null,
                false, BigDecimal.ONE, BigDecimal.ONE,null, BigDecimal.valueOf(50), Categoria.builder().build(), null);

        itemPedido = new ItemPedido(1L, BigDecimal.valueOf(45), BigDecimal.valueOf(90), 2, null, produto);

        pedidoNovo = new Pedido().builder().cliente(cliente).profissional(profissional).horario(OffsetDateTime.parse("2024-05-19T15:30:00-03:00")).build();


        pedidoPago = new Pedido(1L, OffsetDateTime.parse("2024-05-19T15:30:00-03:00"), List.of(itemPedido), StatusPagamento.PAGO,
                FormaPagamento.DINHEIRO, StatusPedido.AGENDADO, cliente, profissional, BigDecimal.valueOf(90), BigDecimal.valueOf(90),
                BigDecimal.valueOf(180),true, BigDecimal.valueOf(100), OffsetDateTime.now(), null, null, null,
                null, null, null, null);


        pedidoSemItemPedido = new Pedido(2L, OffsetDateTime.parse("2024-05-19T15:30:00-03:00"), listaVazia, StatusPagamento.AGUARDANDO_PAGAMENTO,
                null, StatusPedido.AGENDADO, cliente, profissional, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                true, BigDecimal.ZERO, OffsetDateTime.now(), null, null, null,
                null, null, null, null);

        pedidoAguardandoPg = new Pedido(3L, OffsetDateTime.parse("2024-05-19T15:30:00-03:00"), List.of(itemPedido), StatusPagamento.AGUARDANDO_PAGAMENTO,
                null, StatusPedido.AGENDADO, cliente, profissional, BigDecimal.valueOf(90), BigDecimal.valueOf(90), BigDecimal.valueOf(180),
                true, BigDecimal.ZERO, OffsetDateTime.now(), null, null, null,
                null, null, null, null);


        itemPacoteConsumidoAntigo = new ItemPacote(1L, itemPedido, profissional, OffsetDateTime.parse("2024-04-19T15:30:00-03:00"));

        itemPacoteConsumidoRecente = new ItemPacote(2L, itemPedido, profissional, OffsetDateTime.parse("2024-05-19T15:30:00-03:00"));

        itensPacoteConsumidos.addAll(Arrays.asList(itemPacoteConsumidoAntigo, itemPacoteConsumidoRecente));

        pacoteItemConsumido = new Pacote(1L, cliente, OffsetDateTime.parse("2024-05-22T13:00:00-03:00"), 30,
                OffsetDateTime.parse("2024-06-22T13:00:00-03:00"), "2 barbas", null, List.of(),
                itensPacoteConsumidos, List.of());

        pacoteRealizadoEvento = new PacoteRealizadoEvento(pacoteItemConsumido);

        comissao = new Comissao(1L, produto, profissional, BigDecimal.valueOf(45));

    }

    @Test
    public void deveBuscarPedidoPorId() {
        Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoPago));

        Pedido pedidoSalvo = pedidoService.buscarPorId(1L);

        assertEquals(pedidoSalvo, pedidoPago);
        verify(pedidoRepository).findById(1L);
        verifyNoMoreInteractions(pedidoRepository);
    }

    @Test
    public void deveLancarPedidoNaoEncontradoExceptionAoBuscarPedidoPorId() {
        Mockito.when(pedidoRepository.findById(2L)).thenReturn(Optional.empty());

        PedidoNaoEncontradoException exception = Assertions.assertThrows(PedidoNaoEncontradoException.class,
                () -> {pedidoService.buscarPorId(2L);
                });

        assertEquals("Não existe um cadastro de pedido com codigo 2", exception.getMessage());

        verify(pedidoRepository).findById(2L);
        verifyNoMoreInteractions(pedidoRepository);
    }



    @Test
    public void deveAdicionarNovaPedido() {
        Mockito.when(pedidoRepository.save(pedidoPago)).thenReturn(pedidoPago);

        Pedido pedidoSalvo = pedidoService.adicionar(pedidoPago);

        assertEquals(pedidoSalvo, pedidoPago);
        verify(pedidoRepository).save(pedidoPago);
        verifyNoMoreInteractions(pedidoRepository);
    }

    @Test
    public void deveExcluirPedido() {
        pedidoService.remover(1L);

        verify(pedidoRepository).deleteById(1L);
        verifyNoMoreInteractions(pedidoRepository);
    }

    @Test
    public void deveLancarPedidoNaoEncontradoExceptionAoExcluirPedidoComIdInexistente() {
        Mockito.doThrow(new EmptyResultDataAccessException(2)).when(pedidoRepository).deleteById(2L);

        PedidoNaoEncontradoException exception = Assertions.assertThrows(PedidoNaoEncontradoException.class,
                () -> {pedidoService.remover(2L);
                });

        assertEquals("Não existe um cadastro de pedido com codigo 2", exception.getMessage());

        verify(pedidoRepository).deleteById(2L);
        verifyNoMoreInteractions(pedidoRepository);
    }

    @Test
    public void deveLancarEntidadeEmUsoExceptionAoExcluirPedidoEmUso() {
        Mockito.doThrow(new DataIntegrityViolationException("")).when(pedidoRepository).deleteById(1L);

        EntidadeEmUsoException exception = Assertions.assertThrows(EntidadeEmUsoException.class,
                () -> {pedidoService.remover(1L);
                });

        assertEquals("Pedido de código 1 não pode ser removido, pois esta em uso", exception.getMessage());

        verify(pedidoRepository).deleteById(1L);
        verifyNoMoreInteractions(pedidoRepository);
    }

    @Test
    public void deveCriarPedidoPorPacote() {
        Mockito.when(comissaoService.buscarPorProfissionalProduto(1L, 1L)).thenReturn(comissao);
        Mockito.when(comissaoService.calculoComissaoProduto(comissao)).thenReturn(BigDecimal.valueOf(22.5));

        pedidoService.criarPedidoPorPacote(pacoteRealizadoEvento);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCaptor.capture());
        Pedido pedidoSalvo = pedidoCaptor.getValue();

        ArgumentCaptor<Pacote> pacoteCaptor = ArgumentCaptor.forClass(Pacote.class);
        verify(pacoteService).adicionar(pacoteCaptor.capture());
        Pacote pacoteSalvo = pacoteCaptor.getValue();

        assertEquals(pedidoSalvo.getItemPedidos().get(0), itemPacoteConsumidoRecente.getItemPedido());
        assertEquals(pedidoSalvo.getCliente(), cliente);
        assertEquals(pedidoSalvo.getProfissional(), profissional);
        assertEquals(pedidoSalvo.getStatusPedido(), null);
        assertEquals(pedidoSalvo.getValorTotal(), BigDecimal.valueOf(90));
        assertEquals(pedidoSalvo.getComissaoGerada(), BigDecimal.valueOf(45.0));

        Assertions.assertTrue(pacoteSalvo.getItensAtivos().isEmpty());
        Assertions.assertTrue(pacoteSalvo.getItensExpirados().isEmpty());
        Assertions.assertTrue(pacoteSalvo.getItensConsumidos().contains(itemPacoteConsumidoAntigo));
        Assertions.assertTrue(pacoteSalvo.getItensConsumidos().contains(itemPacoteConsumidoRecente));


        verify(pacoteService).adicionar(pacoteSalvo);
    }

    @Test
    public void criar() {
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(profissionalService.buscarPorId(1L)).thenReturn(profissional);

        pedidoService.criar(pedidoNovo);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCaptor.capture());
        Pedido pedidoSalvo = pedidoCaptor.getValue();

        assertEquals(pedidoSalvo.getCliente(), cliente);
        assertEquals(pedidoSalvo.getProfissional(), profissional);
        assertEquals(pedidoSalvo.getStatusPedido(), StatusPedido.AGENDADO);
        assertEquals(pedidoSalvo.getFormaPagamento(), FormaPagamento.AGUARDANDO_PAGAMENTO);
        assertEquals(pedidoSalvo.getComissaoGerada(), BigDecimal.ZERO);
        assertEquals(pedidoSalvo.getValorTotal(), BigDecimal.ZERO);

        verify(pedidoRepository).save(pedidoSalvo);
        verify(clienteService).buscarPorId(1L);
        verify(profissionalService).buscarPorId(1L);
        verifyNoMoreInteractions(pedidoRepository, clienteService, profissionalService);

    }


    //adicionar verify
    @Test
    public void adicionarItemPedido() {
        doReturn(pedidoSemItemPedido).when(pedidoService).buscarPorId(2L);
        when(itemPedidoService.buscarPorId(1L)).thenReturn(itemPedido);
        when(comissaoService.calculoComissaoProduto(any())).thenReturn(BigDecimal.valueOf(22.5));

        pedidoService.adicionarItemPedido(2L, 1L);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCaptor.capture());
        Pedido pedidoSalvo = pedidoCaptor.getValue();

        assertTrue(pedidoSalvo.getItemPedidos().contains(itemPedido));
        assertEquals(pedidoSalvo.getComissaoGerada(), BigDecimal.valueOf(45.0));
        assertEquals(pedidoSalvo.getValorTotal(), BigDecimal.valueOf(90));
        assertEquals(pedidoSalvo.getPontuacaoProfissionalGerada(), BigDecimal.valueOf(90));
        assertEquals(pedidoSalvo.getPontuacaoClienteGerada(), BigDecimal.valueOf(90));
    }

    @Test
    public void deveLancarItemPedidoJaAdicionadoExceptionAoAdicionarItemPedidoRepetidoEmPedido() {
        doReturn(pedidoPago).when(pedidoService).buscarPorId(1L);
        Mockito.when(itemPedidoService.buscarPorId(1L)).thenReturn(itemPedido);

        ItemPedidoJaAdicionadoException exception = Assertions.assertThrows(ItemPedidoJaAdicionadoException.class,
                () -> {pedidoService.adicionarItemPedido(1L, 1L);
                });

        assertEquals("O itemPedido de codigo 1 já foi adicionado a esse pedido", exception.getMessage());

        verify(itemPedidoService).buscarPorId(1L);
        verify(pedidoService).buscarPorId(1L);
        verify(pedidoService).adicionarItemPedido(1L, 1L);
        verifyNoMoreInteractions(itemPedidoService, pedidoService);
    }

    @Test
    public void removerItemPedido() {
    }

    @Test
    public void comissaoPorItem() {
    }

    @Test
    public void gerarPontuacao() {
    }

    @Test
    public void preencherPedido() {
    }

    @Test
    public void realizarPagamentoComPacote() {
    }

    @Test
    public void realizarPagamento() {
    }

    @Test
    public void alterarProfissionalPedido() {
    }

    @Test
    public void confirmarPedido() {
    }
}