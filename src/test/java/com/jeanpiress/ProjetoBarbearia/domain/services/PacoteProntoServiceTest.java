package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.eventos.PacoteProntoCriadoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.PacoteNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PacoteProntoRepository;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PacoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class PacoteProntoServiceTest {

    @Spy
    @InjectMocks
    PacoteProntoService pacoteProntoService;

    @Mock
    PacoteProntoRepository pacoteProntoRepository;

    @Mock
    PacoteService pacoteService;

    @Mock
    ClienteService clienteService;

    @Mock
    PacoteRepository pacoteRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    ItemPacoteService itemPacoteService;


    PacotePronto pacotePronto;
    ItemPacote itemPacote1;
    ItemPacote itemPacote2;
    List<ItemPacote> itensPacote = new ArrayList<>();
    Cliente cliente;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        itemPacote1 = new ItemPacote(1L, ItemPedido.builder().build(), Profissional.builder().build(), OffsetDateTime.now());
        itemPacote2 = new ItemPacote(2L, ItemPedido.builder().build(), Profissional.builder().build(), OffsetDateTime.now());
        itensPacote.addAll(Arrays.asList(itemPacote1, itemPacote2));
        pacotePronto = new PacotePronto(1L, "2 Barbas", null, 31, true, BigDecimal.valueOf(50), BigDecimal.ZERO, BigDecimal.ZERO,
                itensPacote, Categoria.builder().build());

        cliente = new Cliente(1L, "João", "34999999999", LocalDate.parse("1991-11-13"),
                null, BigDecimal.ZERO, null, null, 30, true,
                Profissional.builder().build(), null);
    }

    @Test
    public void deveBuscarPacoteProntoPorId() {
        Mockito.when(pacoteProntoRepository.findById(1L)).thenReturn(Optional.of(pacotePronto));

        PacotePronto pacoteProntoSalvo = pacoteProntoService.buscarPorId(1L);

        assertEquals(pacoteProntoSalvo, pacotePronto);
        assertEquals(pacoteProntoSalvo.getId(), 1L);
        verify(pacoteProntoRepository).findById(1L);
        verifyNoMoreInteractions(pacoteProntoRepository);
    }

    @Test
    public void deveBuscarPacotesAtivos(){
        Mockito.when(pacoteProntoRepository.buscarPacoteProntoAtivo()).thenReturn(List.of(pacotePronto));

        List<PacotePronto> pacotesAtivos = pacoteProntoService.buscarPacotesAtivos();
        PacotePronto pacoteAtivo = pacotesAtivos.get(0);

        assertEquals(pacoteAtivo, pacotePronto);

        verify(pacoteProntoRepository).buscarPacoteProntoAtivo();
        verifyNoMoreInteractions(pacoteProntoRepository);
    }

    @Test
    public void deveLancarPacoteProntoNaoEncontradoExceptionAoBuscarPacoteProntoPorId() {
        Mockito.when(pacoteProntoRepository.findById(2L)).thenReturn(Optional.empty());

        PacoteNaoEncontradoException exception = Assertions.assertThrows(PacoteNaoEncontradoException.class,
                () -> {pacoteProntoService.buscarPorId(2L);
                });

        assertEquals("Não existe um cadastro de pacote com codigo 2", exception.getMessage());

        verify(pacoteProntoRepository).findById(2L);
        verifyNoMoreInteractions(pacoteProntoRepository);
    }

    @Test
    public void deveCriarUmPacotePronto(){
        Mockito.when(itemPacoteService.criarNovosItensPacoteRecebendoListaItemPacote(pacotePronto.getItensAtivos())).thenReturn(itensPacote);

        pacoteProntoService.criarPacotePronto(pacotePronto);

        verify(itemPacoteService).criarNovosItensPacoteRecebendoListaItemPacote(pacotePronto.getItensAtivos());
        verifyNoMoreInteractions(pacoteService);
        verify(pacoteProntoRepository).save(pacotePronto);
        verifyNoMoreInteractions(pacoteProntoRepository);

        ArgumentCaptor<PacoteProntoCriadoEvento> eventCaptor = ArgumentCaptor.forClass(PacoteProntoCriadoEvento.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

    }



    @Test
    public void deveCriarPacoteFinal() {
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        doReturn(pacotePronto).when(pacoteProntoService).buscarPorId(1L);
        when(itemPacoteService.criarNovosItensPacoteRecebendoPacotePronto(pacotePronto)).thenReturn(itensPacote);
        OffsetDateTime dataVencimento = OffsetDateTime.now().plusDays(31);



        ArgumentCaptor<Pacote> pacoteCaptor = ArgumentCaptor.forClass(Pacote.class);
        when(pacoteRepository.save(pacoteCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        pacoteProntoService.criarPacoteFinal(1L, 1L);


        Pacote pacoteSalvo = pacoteCaptor.getValue();
        assertAll("Verificando os valores do pacote",
                () -> assertEquals(cliente, pacoteSalvo.getCliente(), "Cliente"),
                () -> assertEquals(OffsetDateTime.now().getDayOfMonth(), pacoteSalvo.getDataCompra().getDayOfMonth(), "Data Compra"),
                () -> assertEquals(dataVencimento.getDayOfMonth(), pacoteSalvo.getDataVencimento().getDayOfMonth(), "Vencimento"),
                () -> assertEquals(pacotePronto.getValidade(), pacoteSalvo.getValidade(), "Validade"),
                () -> assertEquals(pacotePronto.getNome(), pacoteSalvo.getNome(), "Nome"),
                () -> assertEquals(pacotePronto.getDescricao(), pacoteSalvo.getDescricao(), "Descrição"),
                () -> assertEquals(itensPacote, pacoteSalvo.getItensAtivos(), "Itens Ativos")
        );

        verify(clienteService).buscarPorId(1L);
        verify(pacoteProntoService).buscarPorId(1L);
        verify(itemPacoteService).criarNovosItensPacoteRecebendoPacotePronto(pacotePronto);
        verify(pacoteRepository).save(any(Pacote.class));
        verifyNoMoreInteractions(clienteService, pacoteService, pacoteRepository);
    }


}