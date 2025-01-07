package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.PedidoAlteracaoInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ItemPacoteId;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.PacoteId;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalId;
import com.jeanpiress.ProjetoBarbearia.core.security.CsbSecurity;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPedido;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.RealizacaoItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.eventos.ClienteAtendidoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.eventos.PacoteRealizadoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.*;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PedidoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

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

    @Mock
    ItemPacoteService itemPacoteService;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    UsuarioService usuarioService;

    @Mock
    CsbSecurity security;

    Usuario usuario;
    Pedido pedidoNovo;
    Pedido pedidoPago;
    Pedido pedidoSemItemPedido;
    Pedido pedidoAguardandoPg;
    Pedido pedidoAguardandoPgPorPacote;
    Cliente cliente;
    Produto produto;
    Profissional profissional;
    Profissional profissionalVazio;
    ItemPedido itemPedidoDoisProdutos;
    ItemPedido itemPedidoUmProduto;
    ItemPacote itemPacoteConsumidoRecente;
    ItemPacote itemPacoteConsumidoAntigo;
    ItemPacote itemPacoteAtivo;
    Pacote pacoteItemConsumido;
    Pacote pacoteItensAtivos;
    Pacote pacoteItensAtivosClienteDiferente;
    PacoteRealizadoEvento pacoteRealizadoEvento;
    List<ItemPacote> itensPacoteConsumidos = new ArrayList<>();
    List<ItemPacote> itensPacoteAtivos = new ArrayList<>();
    Comissao comissao;
    List<ItemPedido> listaItemPedidoVazia = new ArrayList<>();
    List<ItemPedido> listaComItemPedidoDoisProdutos = new ArrayList<>();
    List<ItemPedido> listaComItemPedidoUmProduto = new ArrayList<>();
    List<ItemPacote> listaItemPacoteVazia = new ArrayList<>();
    RealizacaoItemPacote realizacaoItemPacote;
    PedidoAlteracaoInput pedidoAlteracaoInput;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente(1L, "João", "34999999999", LocalDate.parse("1991-11-13"),
                null, BigDecimal.ZERO, null, null, 30, true, profissional, null);

        profissional = new Profissional(1L, "João Silva", "João", "34999999999",
                null, LocalDate.parse("1991-11-13"), BigDecimal.ZERO, null, true, null);

        usuario = new Usuario(1L, "joao@csb.com", "123456", "joao", "PROFISSIONAL", null,
                profissional, null);

        produto = new Produto(1L, "corte", BigDecimal.valueOf(45), true, false, null,
                false, BigDecimal.ONE, BigDecimal.ONE,null, BigDecimal.valueOf(50), Categoria.builder().build(), null);

        itemPedidoDoisProdutos = new ItemPedido(1L, BigDecimal.valueOf(45), BigDecimal.valueOf(90), 2, null, produto);

        itemPedidoUmProduto = new ItemPedido(2L, BigDecimal.valueOf(45), BigDecimal.valueOf(45), 1, null, produto);

        listaComItemPedidoDoisProdutos.add(itemPedidoDoisProdutos);
        listaComItemPedidoUmProduto.add(itemPedidoUmProduto);

        pedidoNovo = new Pedido().builder().cliente(cliente).profissional(profissional).horario(OffsetDateTime.now().plusDays(1))
                .formaPagamento(FormaPagamento.AGUARDANDO_PAGAMENTO).isAgendamento(true).build();

        pedidoPago = pedidoCriado(1L, listaComItemPedidoDoisProdutos, StatusPagamento.PAGO, FormaPagamento.DINHEIRO, StatusPedido.FINALIZADO);

        pedidoSemItemPedido = pedidoCriado(2L, listaItemPedidoVazia, StatusPagamento.AGUARDANDO_PAGAMENTO, null, StatusPedido.AGENDADO);

        pedidoAguardandoPg = pedidoCriado(3L, listaComItemPedidoUmProduto, StatusPagamento.AGUARDANDO_PAGAMENTO, null, StatusPedido.AGENDADO);

        pedidoAguardandoPgPorPacote = pedidoCriado(4L, listaComItemPedidoUmProduto, StatusPagamento.AGUARDANDO_PAGAMENTO, null, StatusPedido.AGENDADO);


        itemPacoteConsumidoAntigo = new ItemPacote(1L, itemPedidoDoisProdutos, profissional, OffsetDateTime.parse("2024-04-19T15:30:00-03:00"));

        itemPacoteConsumidoRecente = new ItemPacote(2L, itemPedidoDoisProdutos, profissional, OffsetDateTime.parse("2024-05-19T15:30:00-03:00"));

        itemPacoteAtivo = new ItemPacote(3L, itemPedidoUmProduto, profissional, null);

        itensPacoteConsumidos.addAll(Arrays.asList(itemPacoteConsumidoAntigo, itemPacoteConsumidoRecente));

        itensPacoteAtivos.add(itemPacoteAtivo);

        pacoteItemConsumido = new Pacote(1L, cliente, OffsetDateTime.parse("2024-04-22T13:00:00-03:00"), 30,
                OffsetDateTime.parse("2024-05-22T13:00:00-03:00"), "2 barbas", null, List.of(),
                itensPacoteConsumidos, List.of());

        pacoteItensAtivos = new Pacote(2L, cliente, OffsetDateTime.now().minusMonths(1), 30,
                OffsetDateTime.now().plusDays(1), "2 barbas", null, itensPacoteAtivos,
                listaItemPacoteVazia, List.of());

        pacoteItensAtivosClienteDiferente = new Pacote(3L, Cliente.builder().build(), OffsetDateTime.now().minusMonths(1), 30,
                OffsetDateTime.now().plusDays(1), "2 barbas", null, itensPacoteAtivos,
                listaItemPacoteVazia, List.of());

        pacoteRealizadoEvento = new PacoteRealizadoEvento(pacoteItemConsumido);

        comissao = new Comissao(1L, produto, profissional, BigDecimal.valueOf(45));

        realizacaoItemPacote = new RealizacaoItemPacote(ProfissionalId.builder().id(1L).build(),
                PacoteId.builder().id(2L).build(), ItemPacoteId.builder().id(3L).build());

        pedidoAlteracaoInput = PedidoAlteracaoInput.builder().horario(OffsetDateTime.now().plusDays(1)).
                profissional(ProfissionalId.builder().id(2L).build()).duracao("01:00").build();

        profissionalVazio = Profissional.builder().id(2L).build();
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

        verify(comissaoService).buscarPorProfissionalProduto(1L, 1L);
        verify(comissaoService).calculoComissaoProduto(comissao);
        verify(pacoteService).adicionar(pacoteSalvo);
        verifyNoMoreInteractions(comissaoService, pacoteService);
    }

    @Test
    public void criar() {
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(profissionalService.buscarPorId(1L)).thenReturn(profissional);
        when(usuarioService.buscarPorId(any())).thenReturn(usuario);

        pedidoService.criar(pedidoNovo, "AGENDADO");

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCaptor.capture());
        Pedido pedidoSalvo = pedidoCaptor.getValue();

        assertEquals(pedidoSalvo.getCliente(), cliente);
        assertEquals(pedidoSalvo.getProfissional(), profissional);
        assertEquals(pedidoSalvo.getStatusPedido(), StatusPedido.AGENDADO);
        assertEquals(pedidoSalvo.getFormaPagamento(), FormaPagamento.AGUARDANDO_PAGAMENTO);
        assertEquals(pedidoSalvo.getComissaoGerada(), BigDecimal.ZERO);
        assertEquals(pedidoSalvo.getValorTotal(), BigDecimal.ZERO);
        assertEquals(pedidoSalvo.getCriadoPor(), usuario);
        assertEquals(pedidoSalvo.getCriadoAs().getHour(), OffsetDateTime.now().getHour());

        verify(pedidoRepository).save(pedidoSalvo);
        verify(clienteService).buscarPorId(1L);
        verify(profissionalService).buscarPorId(1L);
        verify(usuarioService).buscarPorId(any());
        verifyNoMoreInteractions(pedidoRepository, clienteService, profissionalService, usuarioService);

    }

    @Test
    public void adicionarItemPedido() {
        doReturn(pedidoSemItemPedido).when(pedidoService).buscarPorId(2L);
        when(itemPedidoService.buscarPorId(1L)).thenReturn(itemPedidoDoisProdutos);
        when(comissaoService.calculoComissaoProduto(any())).thenReturn(BigDecimal.valueOf(22.5));

        pedidoService.adicionarItemPedido(2L, 1L);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCaptor.capture());
        Pedido pedidoSalvo = pedidoCaptor.getValue();

        assertTrue(pedidoSalvo.getItemPedidos().contains(itemPedidoDoisProdutos));
        assertEquals(pedidoSalvo.getComissaoGerada(), BigDecimal.valueOf(90.0));
        assertEquals(pedidoSalvo.getValorTotal(), BigDecimal.valueOf(180));
        assertEquals(pedidoSalvo.getPontuacaoProfissionalGerada(), BigDecimal.valueOf(180));
        assertEquals(pedidoSalvo.getPontuacaoClienteGerada(), BigDecimal.valueOf(180));

        verify(pedidoService).buscarPorId(2L);
        verify(itemPedidoService).buscarPorId(1L);
        verify(comissaoService).calculoComissaoProduto(any());
        verify(pedidoService).adicionarItemPedido(2L, 1L);
        verify(pedidoRepository).save(pedidoSalvo);
        verify(comissaoService).buscarPorProfissionalProduto(1L, 1L);
        verifyNoMoreInteractions(pedidoService, itemPedidoService, comissaoService, pedidoRepository);
    }

    @Test
    public void deveLancarItemPedidoJaAdicionadoExceptionAoAdicionarItemPedidoRepetidoEmPedido() {
        doReturn(pedidoPago).when(pedidoService).buscarPorId(1L);
        Mockito.when(itemPedidoService.buscarPorId(1L)).thenReturn(itemPedidoDoisProdutos);

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
    public void DeveRemoverItemPedido() {
        doReturn(pedidoAguardandoPg).when(pedidoService).buscarPorId(3L);
        when(itemPedidoService.buscarPorId(2L)).thenReturn(itemPedidoUmProduto);
        when(comissaoService.calculoComissaoProduto(any())).thenReturn(BigDecimal.valueOf(22.5));

        pedidoService.removerItemPedido(3L, 2L);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCaptor.capture());
        Pedido pedidoSalvo = pedidoCaptor.getValue();

        assertFalse(pedidoSalvo.getItemPedidos().contains(itemPedidoUmProduto));
        assertEquals(pedidoSalvo.getComissaoGerada(), BigDecimal.valueOf(22.5));
        assertEquals(pedidoSalvo.getValorTotal(), BigDecimal.valueOf(45));
        assertEquals(pedidoSalvo.getPontuacaoProfissionalGerada(), BigDecimal.valueOf(45));
        assertEquals(pedidoSalvo.getPontuacaoClienteGerada(), BigDecimal.valueOf(45));

        verify(pedidoService).buscarPorId(3L);
        verify(itemPedidoService).buscarPorId(2L);
        verify(itemPedidoService).remover(2L);
        verify(comissaoService).calculoComissaoProduto(any());
        verify(pedidoService).removerItemPedido(3L, 2L);
        verify(pedidoRepository).save(pedidoSalvo);
        verify(comissaoService).buscarPorProfissionalProduto(1L, 1L);
        verifyNoMoreInteractions(pedidoService, itemPedidoService, comissaoService, pedidoRepository);

    }

    @Test
    public void deveLancarPedidoNaoContemItemPedidoExceptionAoRemoverItemPedidoNaoPresenteNoPedido() {
        doReturn(pedidoSemItemPedido).when(pedidoService).buscarPorId(2L);
        when(itemPedidoService.buscarPorId(1L)).thenReturn(itemPedidoDoisProdutos);

        PedidoNaoContemItemPedidoException exception = Assertions.assertThrows(PedidoNaoContemItemPedidoException.class,
                () -> {pedidoService.removerItemPedido(2L, 1L);
                });

        assertEquals("O pedido  codigo 2 não possui o item pedido de codigo 1", exception.getMessage());

        verify(itemPedidoService).buscarPorId(1L);
        verify(pedidoService).buscarPorId(2L);
        verify(pedidoService).removerItemPedido(2L, 1L);
        verifyNoMoreInteractions(itemPedidoService, pedidoService);
    }

    @Test
    public void deveRealizarPagamentoComPedidoExistente() {
        doReturn(pedidoAguardandoPgPorPacote).when(pedidoService).buscarPorId(4L);
        when(pacoteService.buscarPorId(2L)).thenReturn(pacoteItensAtivos);
        when(itemPacoteService.buscarPorId(3L)).thenReturn(itemPacoteAtivo);
        when(profissionalService.buscarPorId(1L)).thenReturn(profissional);
        when(usuarioService.buscarPorId(any())).thenReturn(usuario);

        pedidoService.realizarPagamentoComPedidoExistente(realizacaoItemPacote, 4L);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCaptor.capture());
        Pedido pedidoSalvo = pedidoCaptor.getValue();


        ArgumentCaptor<ClienteAtendidoEvento> eventCaptor = ArgumentCaptor.forClass(ClienteAtendidoEvento.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        ClienteAtendidoEvento capturedEvent = eventCaptor.getValue();

        assertEquals(pedidoSalvo.getPontuacaoProfissionalGerada(), BigDecimal.valueOf(45));
        assertEquals(pedidoSalvo.getPontuacaoClienteGerada(), BigDecimal.valueOf(90));
        assertEquals(pedidoSalvo.getStatusPedido(), StatusPedido.FINALIZADO);
        assertEquals(pedidoSalvo.getFormaPagamento(), FormaPagamento.VOUCHER);
        assertEquals(pedidoSalvo.getStatusPagamento(), StatusPagamento.PAGO);
        assertEquals(pedidoSalvo.getValorTotal(), BigDecimal.valueOf(45));
        assertEquals(pedidoSalvo.getDataPagamento().getDayOfMonth(), OffsetDateTime.now().getDayOfMonth());
        assertEquals(pedidoSalvo.getRecibidoPor(), usuario);


        assertEquals(capturedEvent.getCliente(), cliente);

        verify(pacoteService).adicionar(any());
        verify(pacoteService).buscarPorId(2L);
        verify(pacoteService).alterarAtivoParaConsumido(any(), any(), any());
        verify(pedidoService).buscarPorId(4L);
        verify(pedidoService).realizarPagamentoComPedidoExistente(realizacaoItemPacote, 4L);
        verify(usuarioService).buscarPorId(any());
        verifyNoMoreInteractions(pacoteService, pedidoService, pedidoRepository, usuarioService);

    }

    @Test
    public void deveLancarPedidoJaFoiPagoExceptionSeStatusPedidoForPago(){
        doReturn(pedidoPago).when(pedidoService).buscarPorId(1L);

        PedidoJaFoiPagoException exception = Assertions.assertThrows(PedidoJaFoiPagoException.class,
                () -> {pedidoService.realizarPagamentoComPedidoExistente(realizacaoItemPacote, 1L);
                });

        assertEquals("O pedido de codigo 1 já consta como pago", exception.getMessage());

        verify(pedidoService).buscarPorId(1L);
        verify(pedidoService).realizarPagamentoComPedidoExistente(realizacaoItemPacote, 1L);
        verifyNoMoreInteractions(pedidoService, pedidoService);
    }

    @Test
    public void deveLancarPacoteVencidoExceptionSePacoteEstiverVencido(){
        doReturn(pedidoAguardandoPgPorPacote).when(pedidoService).buscarPorId(4L);
        when(pacoteService.buscarPorId(any())).thenReturn(pacoteItemConsumido);

        PacoteVencidoException exception = Assertions.assertThrows(PacoteVencidoException.class,
                () -> {pedidoService.realizarPagamentoComPedidoExistente(realizacaoItemPacote, 4L);
                });

        assertEquals("O pacote de codigo 1 esta vencido", exception.getMessage());

        verify(pedidoService).buscarPorId(4L);
        verify(pacoteService).buscarPorId(any());
        verify(pedidoService).realizarPagamentoComPedidoExistente(realizacaoItemPacote, 4L);
        verifyNoMoreInteractions(pedidoService, pacoteService);

    }

    @Test
    public void deveLancarClientesDiferentesExceptionSeClienteDoPacoteNaoForClienteDoPedido(){
        doReturn(pedidoAguardandoPgPorPacote).when(pedidoService).buscarPorId(4L);
        when(pacoteService.buscarPorId(any())).thenReturn(pacoteItensAtivosClienteDiferente);

        ClientesDiferentesException exception = Assertions.assertThrows(ClientesDiferentesException.class,
                () -> {pedidoService.realizarPagamentoComPedidoExistente(realizacaoItemPacote, 4L);
                });

        assertEquals("Cliente do pedido não é o titular do pacote", exception.getMessage());

        verify(pedidoService).buscarPorId(4L);
        verify(pacoteService).buscarPorId(any());
        verify(pedidoService).realizarPagamentoComPedidoExistente(realizacaoItemPacote, 4L);
        verifyNoMoreInteractions(pedidoService, pacoteService);
    }

    @Test
    public void deveLancarItemPacoteNaoEncontradoEmItemAtivoExceptionSeItemPacoteNaoConstarEmItensPacotesAtivos(){
        realizacaoItemPacote.setItemPacote(ItemPacoteId.builder().id(2L).build());
        doReturn(pedidoAguardandoPgPorPacote).when(pedidoService).buscarPorId(4L);
        when(pacoteService.buscarPorId(2L)).thenReturn(pacoteItensAtivos);
        when(itemPacoteService.buscarPorId(2L)).thenReturn(itemPacoteConsumidoRecente);

        ItemPacoteNaoEncontradoEmItemAtivoException exception = Assertions.assertThrows(ItemPacoteNaoEncontradoEmItemAtivoException.class,
                () -> {pedidoService.realizarPagamentoComPedidoExistente(realizacaoItemPacote, 4L);
                });

        assertEquals("Não existe um itemPacote de codigo 2 listado em itensAtivos", exception.getMessage());

        verify(pedidoService).buscarPorId(4L);
        verify(pacoteService).buscarPorId(2L);
        verify(itemPacoteService).buscarPorId(2L);
        verify(pedidoService).realizarPagamentoComPedidoExistente(realizacaoItemPacote, 4L);
        verifyNoMoreInteractions(pedidoService, pacoteService, itemPacoteService);
    }


    @Test
    public void deveRealizarPagamentoComDinheiro() {
        doReturn(pedidoAguardandoPg).when(pedidoService).buscarPorId(3L);
        when(pedidoRepository.save(any())).thenReturn(pedidoAguardandoPg);
        when(usuarioService.buscarPorId(any())).thenReturn(usuario);

        pedidoService.realizarPagamento("dinheiro", 3L);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCaptor.capture());
        Pedido pedidoSalvo = pedidoCaptor.getValue();

        ArgumentCaptor<ClienteAtendidoEvento> eventCaptor = ArgumentCaptor.forClass(ClienteAtendidoEvento.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        ClienteAtendidoEvento capturedEvent = eventCaptor.getValue();

        assertEquals(pedidoSalvo.getFormaPagamento(), FormaPagamento.DINHEIRO);
        assertEquals(pedidoSalvo.getDataPagamento().getDayOfMonth(), OffsetDateTime.now().getDayOfMonth());
        assertEquals(pedidoSalvo.getStatusPagamento(), StatusPagamento.PAGO);
        assertEquals(pedidoSalvo.getCliente(), capturedEvent.getCliente());
        assertEquals(pedidoSalvo.getRecibidoPor(), usuario);

        verify(pedidoService).buscarPorId(3L);
        verify(pedidoRepository).save(any());
        verify(pedidoService).realizarPagamento("dinheiro", 3L);
        verify(usuarioService).buscarPorId(any());
        verifyNoMoreInteractions(pedidoRepository, eventPublisher, pedidoService, usuarioService);

    }


    @Test
    public void deveRealizarPagamentoComPix() {
        doReturn(pedidoAguardandoPg).when(pedidoService).buscarPorId(3L);
        when(pedidoRepository.save(any())).thenReturn(pedidoAguardandoPg);
        when(usuarioService.buscarPorId(any())).thenReturn(usuario);

        pedidoService.realizarPagamento("Pix", 3L);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCaptor.capture());
        Pedido pedidoSalvo = pedidoCaptor.getValue();

        ArgumentCaptor<ClienteAtendidoEvento> eventCaptor = ArgumentCaptor.forClass(ClienteAtendidoEvento.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        ClienteAtendidoEvento capturedEvent = eventCaptor.getValue();

        assertEquals(pedidoSalvo.getFormaPagamento(), FormaPagamento.PIX);
        assertEquals(pedidoSalvo.getDataPagamento().getDayOfMonth(), OffsetDateTime.now().getDayOfMonth());
        assertEquals(pedidoSalvo.getStatusPagamento(), StatusPagamento.PAGO);
        assertEquals(pedidoSalvo.getCliente(), capturedEvent.getCliente());
        assertEquals(pedidoSalvo.getRecibidoPor(), usuario);

        verify(pedidoService).buscarPorId(3L);
        verify(pedidoRepository).save(any());
        verify(pedidoService).realizarPagamento("Pix", 3L);
        verify(usuarioService).buscarPorId(any());
        verifyNoMoreInteractions(pedidoRepository, eventPublisher, pedidoService, usuarioService);
    }

    @Test
    public void deveRealizarPagamentoComDebito() {
        doReturn(pedidoAguardandoPg).when(pedidoService).buscarPorId(3L);
        when(pedidoRepository.save(any())).thenReturn(pedidoAguardandoPg);
        when(usuarioService.buscarPorId(any())).thenReturn(usuario);

        pedidoService.realizarPagamento("Debito", 3L);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCaptor.capture());
        Pedido pedidoSalvo = pedidoCaptor.getValue();

        ArgumentCaptor<ClienteAtendidoEvento> eventCaptor = ArgumentCaptor.forClass(ClienteAtendidoEvento.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        ClienteAtendidoEvento capturedEvent = eventCaptor.getValue();

        assertEquals(pedidoSalvo.getFormaPagamento(), FormaPagamento.DEBITO);
        assertEquals(pedidoSalvo.getDataPagamento().getDayOfMonth(), OffsetDateTime.now().getDayOfMonth());
        assertEquals(pedidoSalvo.getStatusPagamento(), StatusPagamento.PAGO);
        assertEquals(pedidoSalvo.getCliente(), capturedEvent.getCliente());
        assertEquals(pedidoSalvo.getRecibidoPor(), usuario);

        verify(pedidoService).buscarPorId(3L);
        verify(pedidoRepository).save(any());
        verify(pedidoService).realizarPagamento("Debito", 3L);
        verify(usuarioService).buscarPorId(any());
        verifyNoMoreInteractions(pedidoRepository, eventPublisher, pedidoService, usuarioService);
    }

    @Test
    public void deveRealizarPagamentoComCredito() {
        doReturn(pedidoAguardandoPg).when(pedidoService).buscarPorId(3L);
        when(pedidoRepository.save(any())).thenReturn(pedidoAguardandoPg);
        when(usuarioService.buscarPorId(any())).thenReturn(usuario);

        pedidoService.realizarPagamento("Credito", 3L);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCaptor.capture());
        Pedido pedidoSalvo = pedidoCaptor.getValue();

        ArgumentCaptor<ClienteAtendidoEvento> eventCaptor = ArgumentCaptor.forClass(ClienteAtendidoEvento.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        ClienteAtendidoEvento capturedEvent = eventCaptor.getValue();

        assertEquals(pedidoSalvo.getFormaPagamento(), FormaPagamento.CREDITO);
        assertEquals(pedidoSalvo.getDataPagamento().getDayOfMonth(), OffsetDateTime.now().getDayOfMonth());
        assertEquals(pedidoSalvo.getStatusPagamento(), StatusPagamento.PAGO);
        assertEquals(pedidoSalvo.getCliente(), capturedEvent.getCliente());
        assertEquals(pedidoSalvo.getRecibidoPor(), usuario);

        verify(pedidoService).buscarPorId(3L);
        verify(pedidoRepository).save(any());
        verify(pedidoService).realizarPagamento("Credito", 3L);
        verify(usuarioService).buscarPorId(any());
        verifyNoMoreInteractions(pedidoRepository, eventPublisher, pedidoService, usuarioService);

    }

    @Test
    public void deveLancarOperacaoNaoRealizadaExceptionAoTentarFazerPagentoComoVoucher() {
        doReturn(pedidoAguardandoPg).when(pedidoService).buscarPorId(3L);

        OperacaoNaoRealizadaException exception = Assertions.assertThrows(OperacaoNaoRealizadaException.class,
                () -> {pedidoService.realizarPagamento("voucher", 3L);
                });

        assertEquals("Operação não pode ser realizada, existe outro recurso para essa operação", exception.getMessage());

        verify(pedidoService).buscarPorId(3L);
        verify(pedidoService).realizarPagamento("voucher", 3L);
        verifyNoMoreInteractions(pedidoService);

    }

    @Test
    public void deveLancarOperacaoNaoRealizadaExceptionAoTentarFazerPagentoComoPonto() {
        doReturn(pedidoAguardandoPg).when(pedidoService).buscarPorId(3L);

        OperacaoNaoRealizadaException exception = Assertions.assertThrows(OperacaoNaoRealizadaException.class,
                () -> {pedidoService.realizarPagamento("ponto", 3L);
                });

        assertEquals("Operação não pode ser realizada, existe outro recurso para essa operação", exception.getMessage());

        verify(pedidoService).buscarPorId(3L);
        verify(pedidoService).realizarPagamento("ponto", 3L);
        verifyNoMoreInteractions(pedidoService);

    }

    @Test
    public void deveFormaPagamentoNaoReconhecidaExceptionAoTentarFazerPagentoComFormaDePagamentoNaoCadastrada() {
        doReturn(pedidoAguardandoPg).when(pedidoService).buscarPorId(3L);

        FormaPagamentoNaoReconhecidaException exception = Assertions.assertThrows(FormaPagamentoNaoReconhecidaException.class,
                () -> {pedidoService.realizarPagamento("ouro", 3L);
                });

        assertEquals("Forma de pagamento não reconhecida", exception.getMessage());

        verify(pedidoService).buscarPorId(3L);
        verify(pedidoService).realizarPagamento("ouro", 3L);
        verifyNoMoreInteractions(pedidoService);
    }

    @Test
    public void deveAlterarProfissionalPedido() {
        doReturn(pedidoAguardandoPg).when(pedidoService).buscarPorId(3L);
        when(profissionalService.buscarPorId(2L)).thenReturn(profissionalVazio);
        when(usuarioService.buscarPorId(any())).thenReturn(usuario);

        pedidoService.alterarInfoPedido(pedidoAlteracaoInput, 3L);

        assertEquals(pedidoAguardandoPg.getProfissional(), profissionalVazio);
        assertEquals(pedidoAguardandoPg.getHorario().getDayOfMonth(), OffsetDateTime.now().plusDays(1).getDayOfMonth());
        assertEquals(pedidoAguardandoPg.getAlteradoPor(), usuario);
        assertEquals(pedidoAguardandoPg.getModificadoAs().getHour(), OffsetDateTime.now().getHour());

        verify(pedidoService).buscarPorId(3L);
        verify(profissionalService).buscarPorId(2L);
        verify(pedidoService).alterarInfoPedido(pedidoAlteracaoInput, 3L);
        verify(usuarioService).buscarPorId(any());
        verifyNoMoreInteractions(pedidoService, profissionalService, usuarioService);

    }

    @Test
    public void deveLancarPedidoJaFoiPagoExceptionCasoTenteMudarProfissionalDePedidoComStatusPagamentoPago(){
        doReturn(pedidoPago).when(pedidoService).buscarPorId(1L);

        PedidoJaFoiPagoException exception = Assertions.assertThrows(PedidoJaFoiPagoException.class,
                () -> {pedidoService.alterarInfoPedido(pedidoAlteracaoInput, 1L);
                });

        assertEquals("Não é permitido alterar profissional de pedido já recebido", exception.getMessage());

        verify(pedidoService).buscarPorId(1L);
        verify(pedidoService).alterarInfoPedido(pedidoAlteracaoInput, 1L);
        verifyNoMoreInteractions(pedidoService);
    }

    @Test
    public void deveLancarHorarioInvalidoExceptionCasoTenteMudarHorarioParaUmaDataAnteriorAoAgora(){
        doReturn(pedidoAguardandoPg).when(pedidoService).buscarPorId(3L);
        pedidoAlteracaoInput.setHorario(OffsetDateTime.now().minusDays(1));

        HorarioInvalidoException exception = Assertions.assertThrows(HorarioInvalidoException.class,
                () -> {pedidoService.alterarInfoPedido(pedidoAlteracaoInput, 3L);
                });

        assertEquals("Não é possivel marcar um horario em uma data que já passou", exception.getMessage());

        verify(pedidoService).buscarPorId(3L);
        verify(pedidoService).alterarInfoPedido(pedidoAlteracaoInput, 3L);
        verifyNoMoreInteractions(pedidoService);
    }


    @Test
    public void deveCancelarPedidoAgendadoPorQualquerUsuario(){
        Permissao permissao = new Permissao(2L, "RECEPCAO", null, Set.of(usuario));
        usuario.setPermissoes(Set.of(permissao));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoSemItemPedido));
        when(usuarioService.buscarPorId(any())).thenReturn(usuario);
        when(pedidoRepository.save(pedidoSemItemPedido)).thenReturn(pedidoSemItemPedido);

        pedidoService.cancelarPedido(1L);

        assertEquals(pedidoSemItemPedido.getStatusPedido(), StatusPedido.CANCELADO);
        assertEquals(pedidoSemItemPedido.getStatusPagamento(), StatusPagamento.AGUARDANDO_PAGAMENTO);
        assertEquals(pedidoSemItemPedido.getCanceladoAs().getHour(), OffsetDateTime.now().getHour());
        assertEquals(pedidoSemItemPedido.getCanceladoPor(), usuario);

        verify(pedidoRepository).findById(1L);
        verify(usuarioService).buscarPorId(any());
        verify(pedidoRepository).save(pedidoSemItemPedido);
        verifyNoMoreInteractions(usuarioService, pedidoRepository);

    }

    @Test
    public void deveLancarPedidoNaoPodeSerCanceladoExceptionAoTentarCancelarUmPedidoJaRecebido(){
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoPago));
        Permissao permissao = new Permissao(2L, "RECEPCAO", null, Set.of(usuario));
        usuario.setPermissoes(Set.of(permissao));
        when(usuarioService.buscarPorId(any())).thenReturn(usuario);

        PedidoNaoPodeSerCanceladoException exception = Assertions.assertThrows(PedidoNaoPodeSerCanceladoException.class,
                () -> {pedidoService.cancelarPedido(1L);
                });

        assertEquals("Apenas os gerentes do sistema pode cancelar pedidos pagos", exception.getMessage());

        verify(pedidoRepository).findById(1L);
        verify(usuarioService).buscarPorId(any());
        verifyNoMoreInteractions(pedidoRepository, usuarioService);
    }

    @Test
    public void deveCancelarPedidoPagoComUsuairoGerente(){
        usuario.setMaiorPermissao("GERENTE");
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoPago));
        when(usuarioService.buscarPorId(any())).thenReturn(usuario);

        pedidoService.cancelarPedido(1L);

        assertEquals(pedidoPago.getStatusPedido(), StatusPedido.CANCELADO);
        assertEquals(pedidoPago.getStatusPagamento(), StatusPagamento.AGUARDANDO_PAGAMENTO);
        assertEquals(pedidoPago.getCanceladoAs().getHour(), OffsetDateTime.now().getHour());
        assertEquals(pedidoPago.getCanceladoPor(), usuario);

        verify(pedidoRepository).findById(1L);
        verify(usuarioService).buscarPorId(any());
        verify(pedidoRepository).save(pedidoPago);
        verifyNoMoreInteractions(usuarioService, pedidoRepository);

    }

    private Pedido pedidoCriado(Long id, List<ItemPedido> itensPedidos, StatusPagamento statusPagamento,
                                FormaPagamento formaPagamento, StatusPedido statusPedido) {
        Pedido pedido = Pedido.builder()
                .id(id)
                .horario(OffsetDateTime.parse("2024-05-19T15:30:00-03:00"))
                .itemPedidos(itensPedidos)
                .statusPagamento(statusPagamento)
                .formaPagamento(formaPagamento)
                .statusPedido(statusPedido)
                .cliente(cliente)
                .profissional(profissional)
                .comissaoGerada(BigDecimal.valueOf(45))
                .pontuacaoProfissionalGerada(BigDecimal.valueOf(90))
                .pontuacaoClienteGerada(BigDecimal.valueOf(90))
                .caixaAberto(true)
                .valorTotal(BigDecimal.valueOf(90))
                .dataPagamento(LocalDateTime.now())
                .isAgendamento(true)
                .duracao("01:00")
                .build();

        return pedido;
    }
}