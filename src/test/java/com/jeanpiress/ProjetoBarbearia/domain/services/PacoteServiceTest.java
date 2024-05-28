package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ItemPacoteId;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.PacoteId;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalId;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.RealizacaoItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.eventos.PacoteRealizadoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.*;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ClienteRepository;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PacoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
class PacoteServiceTest {

    @Spy
    @InjectMocks
    PacoteService pacoteService;

    @Mock
    PacoteRepository pacoteRepository;

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    ProfissionalService profissionalService;

    @Mock
    ItemPacoteService itemPacoteService;

    @Mock
    ApplicationEventPublisher eventPublisher;

    Pacote pacote;
    Pacote pacoteItensAtivosVazio;
    Pacote pacoteExpirado;
    ItemPacote itemPacote;
    List<ItemPacote> itensPacote = new ArrayList<>();
    List<ItemPacote> itensPacoteAtivos = new ArrayList<>();
    List<ItemPacote> itensPacoteAtivosVazio = new ArrayList<>();
    List<ItemPacote> itensPacoteConsumidos = new ArrayList<>();
    List<ItemPacote> itensPacoteExpirados = new ArrayList<>();
    List<Pacote> pacotes = new ArrayList<>();
    ItemPedido itemPedido;
    RealizacaoItemPacote realizacaoItemPacote;
    Profissional profissional;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        itemPacote = new ItemPacote(1L, ItemPedido.builder().build(), Profissional.builder().build(), null);
        itensPacoteAtivos.add(itemPacote);
        itensPacote.add(itemPacote);

        pacote = new Pacote(1L, Cliente.builder().build(), OffsetDateTime.parse("2024-05-22T13:00:00-03:00"), 30,
                OffsetDateTime.parse("2024-06-22T13:00:00-03:00"), "2 barbas", null, itensPacoteAtivos,
                itensPacoteConsumidos, itensPacoteExpirados);

        pacoteItensAtivosVazio = new Pacote(2L, Cliente.builder().build(), OffsetDateTime.parse("2024-04-22T13:00:00-03:00"), 30,
                null, "2 barbas", null, itensPacoteAtivosVazio, itensPacoteConsumidos, itensPacoteExpirados);

        pacoteExpirado = new Pacote(3L, Cliente.builder().build(), OffsetDateTime.parse("2024-04-22T13:00:00-03:00"), 30,
                OffsetDateTime.parse("2024-05-22T13:00:00-03:00"), "2 barbas", null, itensPacoteAtivos,
                itensPacoteConsumidos, itensPacoteExpirados);

        pacotes.add(pacote);
        itemPedido = new ItemPedido(1L, null, null, 2, null, Produto.builder().build());

        realizacaoItemPacote = new RealizacaoItemPacote(ProfissionalId.builder().id(1L).build(), PacoteId.builder().id(1L).build(),
                ItemPacoteId.builder().id(1L).build());

        profissional = new Profissional(1L, "João Silva", "João", "34999999999", null,
                OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), BigDecimal.ZERO, null, true, null);
    }

    @Test
    public void deveBuscarPacotePorId() {
        when(pacoteRepository.findById(1L)).thenReturn(Optional.of(pacote));

        Pacote pacoteRetornado = pacoteService.buscarPorId(1L);

        assertEquals(pacoteRetornado, pacote);
        verify(pacoteRepository).findById(1L);
        verifyNoMoreInteractions(pacoteRepository);
    }

    @Test
    public void deveLancarPacoteNaoEncontradoExceptionAoBuscarPacotePorId() {
        when(pacoteRepository.findById(2L)).thenReturn(Optional.empty());

        PacoteNaoEncontradoException exception = Assertions.assertThrows(PacoteNaoEncontradoException.class,
                () -> {pacoteService.buscarPorId(2L);
                });

        assertEquals("Não existe um cadastro de pacote com codigo 2", exception.getMessage());

        verify(pacoteRepository).findById(2L);
        verifyNoMoreInteractions(pacoteRepository);
    }

    @Test
    public void deveBuscarListaPacotePorCliente() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        when(pacoteRepository.findByClienteId(1L)).thenReturn(pacotes);

        List<Pacote> pacotesRetornardos = pacoteService.buscarPorClinte(1L);

        assertEquals(pacotesRetornardos.get(0), pacotes.get(0));
        verify(pacoteRepository).findByClienteId(1L);
        verify(clienteRepository).existsById(1L);
        verifyNoMoreInteractions(pacoteRepository, clienteRepository);
    }

    @Test
    public void deveLancarClienteNaoEncontradoExceptionAoBuscarListaPacotePorClienteInexistente() {
        when(clienteRepository.existsById(2L)).thenReturn(false);

        ClienteNaoEncontradoException exception = Assertions.assertThrows(ClienteNaoEncontradoException.class,
                () -> {pacoteService.buscarPorClinte(2L);
                });

        assertEquals("Não existe um cadastro de cliente com codigo 2", exception.getMessage());

        verify(clienteRepository).existsById(2L);
        verifyNoMoreInteractions(clienteRepository);
    }

    @Test
    public void deveAdicionarNovoPacote() {
        Mockito.when(pacoteRepository.save(pacote)).thenReturn(pacote);

        Pacote pacoteSalvo = pacoteService.adicionar(pacote);

        assertEquals(pacoteSalvo, pacote);
        verify(pacoteRepository).save(pacote);
        verifyNoMoreInteractions(pacoteRepository);
    }

    @Test
    public void deveBuscarListaPacoteComItensAtivos() {
        when(pacoteRepository.findAllComItensAtivos()).thenReturn(pacotes);

        List<Pacote> pacotesRetornardos = pacoteService.buscarPacotesComItensAtivos();

        assertEquals(pacotesRetornardos.get(0), pacotes.get(0));
        verify(pacoteRepository).findAllComItensAtivos();
        verifyNoMoreInteractions(pacoteRepository);
    }

    @Test
    public void deveBuscarListaPacoteComItensExpirados() {
        when(pacoteRepository.findAllComItensExpirados()).thenReturn(pacotes);

        List<Pacote> pacotesRetornardos = pacoteService.buscarPacotesComItensExpirados();

        assertEquals(pacotesRetornardos.get(0), pacotes.get(0));
        verify(pacoteRepository).findAllComItensExpirados();
        verifyNoMoreInteractions(pacoteRepository);
    }


    @Test
    public void deveReceberUmItemDoPacote() {
        when(profissionalService.buscarPorId(1L)).thenReturn(profissional);
        doReturn(pacote).when(pacoteService).buscarPorId(1L);
        when(itemPacoteService.buscarPorId(1L)).thenReturn(itemPacote);

        Pacote pacoteAlterado = pacoteService.realizarUmItemDoPacote(realizacaoItemPacote);

        ArgumentCaptor<PacoteRealizadoEvento> eventCaptor = ArgumentCaptor.forClass(PacoteRealizadoEvento.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        PacoteRealizadoEvento capturedEvent = eventCaptor.getValue();

        assertTrue(pacoteAlterado.getItensAtivos().isEmpty());
        assertTrue(pacoteAlterado.getItensConsumidos().contains(itemPacote));
        assertEquals(capturedEvent.getPacote(), pacoteAlterado);

        verify(profissionalService).buscarPorId(1L);

        verifyNoMoreInteractions(pacoteRepository, profissionalService, eventPublisher);
    }

    @Test
    public void deveLancarItemPacoteNaoEncontradoEmItemAtivoExceptionSePacoteNaoPassuirItemPacoteInformado() {
        doReturn(pacote).when(pacoteService).buscarPorId(1L);

        ItemPacoteNaoEncontradoEmItemAtivoException exception = Assertions.assertThrows(
                ItemPacoteNaoEncontradoEmItemAtivoException.class, () -> {pacoteService.realizarUmItemDoPacote(realizacaoItemPacote);});

        assertEquals("Não existe um itemPacote de codigo 1 listado em itensAtivos", exception.getMessage());

        verifyNoMoreInteractions(pacoteRepository);

    }

    @Test
    public void deveLancarPacoteVencidoExceptionSePacoteEstiverVencido() {
        realizacaoItemPacote.setPacote(PacoteId.builder().id(3L).build());
        doReturn(pacoteExpirado).when(pacoteService).buscarPorId(3L);
        when(itemPacoteService.buscarPorId(1L)).thenReturn(itemPacote);

        PacoteVencidoException exception = Assertions.assertThrows(
                PacoteVencidoException.class, () -> {pacoteService.realizarUmItemDoPacote(realizacaoItemPacote);});

        assertEquals("O pacote de codigo 3 esta vencido", exception.getMessage());

        verifyNoMoreInteractions(pacoteRepository);

    }

    @Test
    public void deveLancarPacoteNaoPossuiItensAtivosExceptionSeNaoExistirItemAtivo() {
        PacoteNaoPossuiItensAtivosException exception = Assertions.assertThrows(
                PacoteNaoPossuiItensAtivosException.class, () -> {pacoteService.alterarAtivoParaConsumido(pacoteItensAtivosVazio,
                        profissional, 1L);});

        assertEquals("O pacote de codigo 2 não possui itens ativos", exception.getMessage());


    }

    @Test
    public void deveMudarOsItensDeAtivosParaConsumidoSeDataDeVencimentoEstiverExpirado() {
        doReturn(List.of(pacoteExpirado)).when(pacoteService).buscarPacotesComItensAtivos();

        pacoteService.verificarValidadePacote();

        Pacote pacoteTeste = List.of(pacoteExpirado).get(0);

        assertTrue(pacoteTeste.getItensAtivos().isEmpty());
        assertTrue(pacoteTeste.getItensExpirados().contains(itemPacote));
        assertTrue(itemPacote.getDataConsumo().isBefore(OffsetDateTime.now().plusSeconds(10)));

        verify(pacoteRepository).saveAll(anyList());

    }

    @Test
    public void naoDeveMudarOsItensDeAtivosParaConsumidoSeDataDeVencimentoNaoEstiverExpirado() {
        doReturn(pacotes).when(pacoteService).buscarPacotesComItensAtivos();

        pacoteService.verificarValidadePacote();

        Pacote pacoteTeste = pacotes.get(0);

        assertTrue(pacoteTeste.getItensAtivos().contains(itemPacote));
        assertFalse(pacoteTeste.getItensExpirados().contains(itemPacote));
        assertTrue(itemPacote.getDataConsumo() == null);

        verify(pacoteRepository).saveAll(anyList());

    }



}