package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ItemPacoteNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ItemPacoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
class ItemPacoteServiceTest {

    @InjectMocks
    ItemPacoteService itemPacoteService;

    @Mock
    ItemPacoteRepository itemPacoteRepository;

    @Mock
    ItemPedidoService itemPedidoService;

    ItemPacote itemPacote;
    ItemPedido itemPedido;
    Profissional profissional;
    PacotePronto pacotePronto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        profissional = new Profissional(1L, "João Silva", "João", "34999999999",
                null, LocalDate.parse("1991-11-13"), BigDecimal.ZERO, null, true, null);
        itemPedido = new ItemPedido(1L, null, null, 2, null, Produto.builder().build());
        itemPacote = new ItemPacote(1L, itemPedido, profissional, null);
        pacotePronto = new PacotePronto(1L, "2 barbas", null, 30, true, BigDecimal.valueOf(50),
                BigDecimal.ZERO, BigDecimal.ZERO, List.of(itemPacote), Categoria.builder().build());

    }

    @Test
    public void deveBuscarItemPacotePorId() {
        when(itemPacoteRepository.findById(1L)).thenReturn(Optional.of(itemPacote));

        ItemPacote iP = itemPacoteService.buscarPorId(1L);

        assertEquals(iP, itemPacote);
        verify(itemPacoteRepository).findById(1L);
        verifyNoMoreInteractions(itemPacoteRepository);
    }

    @Test
    public void deveLancarItemPacoteNaoEncontradoExceptionAoBuscarItemPacotePorId() {
        when(itemPacoteRepository.findById(2L)).thenReturn(Optional.empty());

        ItemPacoteNaoEncontradoException exception = assertThrows(ItemPacoteNaoEncontradoException.class,
                () -> {itemPacoteService.buscarPorId(2L);
                });

        assertEquals("Não existe um cadastro de itemPacote com codigo 2", exception.getMessage());

        verify(itemPacoteRepository).findById(2L);
        verifyNoMoreInteractions(itemPacoteRepository);
    }

    @Test
    public void deveCriarNovosItensPacoteComNovoIdRecebendoListaItemPacote(){
        when(itemPedidoService.buscarListaComIds(List.of(1L))).thenReturn(List.of(itemPedido));
        when(itemPacoteRepository.saveAll(anyList())).thenAnswer(invocation -> {
            return invocation.<List<ItemPacote>>getArgument(0);
        });

        List<ItemPacote> itensPacoteSalvo = itemPacoteService.criarNovosItensPacoteRecebendoListaItemPacote(List.of(itemPacote));
        ItemPacote itemPacoteSalvo = itensPacoteSalvo.get(0);

        assertNull(itemPacoteSalvo.getId());
        assertEquals(itemPacoteSalvo.getItemPedido(), itemPacote.getItemPedido());
        assertNull(itemPacoteSalvo.getProfissional());
        assertNull(itemPacoteSalvo.getDataConsumo());

        verify(itemPedidoService).buscarListaComIds(List.of(1L));
        verify(itemPacoteRepository).saveAll(anyList());
        verifyNoMoreInteractions(itemPedidoService, itemPacoteRepository);
    }

    @Test
    public void deveCriarNovosItensPacoteComNovoIdRecebendoPacotePronto(){
        when(itemPedidoService.buscarListaComIds(List.of(1L))).thenReturn(List.of(itemPedido));
        when(itemPacoteRepository.saveAll(anyList())).thenAnswer(invocation -> {
            return invocation.<List<ItemPacote>>getArgument(0);
        });

        List<ItemPacote> itensPacoteSalvo = itemPacoteService.criarNovosItensPacoteRecebendoPacotePronto(pacotePronto);
        ItemPacote itemPacoteSalvo = itensPacoteSalvo.get(0);

        assertNull(itemPacoteSalvo.getId());
        assertEquals(itemPacoteSalvo.getItemPedido(), itemPacote.getItemPedido());
        assertNull(itemPacoteSalvo.getProfissional());
        assertNull(itemPacoteSalvo.getDataConsumo());

        verify(itemPedidoService).buscarListaComIds(List.of(1L));
        verify(itemPacoteRepository).saveAll(anyList());
        verifyNoMoreInteractions(itemPedidoService, itemPacoteRepository);
    }


}