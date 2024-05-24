package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ItemPedidoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ItemPedidoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
class ItemPedidoServiceTest {

    @InjectMocks
    ItemPedidoService itemPedidoService;

    @Mock
    ItemPedidoRepository itemPedidoRepository;

    @Mock
    ProdutoService produtoService;

    ItemPedido itemPedido;
    Produto produto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        produto = new Produto(1L, "corte", BigDecimal.valueOf(45), true, false, null,
                false, BigDecimal.ONE, BigDecimal.ONE,null, BigDecimal.valueOf(50),
                Categoria.builder().build(), null);

        itemPedido = new ItemPedido(1L, null, null, 2, null, produto);
    }

    @Test
    public void deveBuscarItemPedidoPorId() {
        Mockito.when(itemPedidoRepository.findById(1L)).thenReturn(Optional.of(itemPedido));

        ItemPedido iP = itemPedidoService.buscarPorId(1L);

        assertEquals(iP, itemPedido);
        verify(itemPedidoRepository).findById(1L);
        verifyNoMoreInteractions(itemPedidoRepository);
    }

    @Test
    public void deveBuscarListaItensPedidoRecebendoListaIds(){
        Mockito.when(itemPedidoRepository.findAllById(List.of(1L))).thenReturn(List.of(itemPedido));

        List<ItemPedido> itensPedido = itemPedidoService.buscarListaComIds(List.of(1L));

        assertEquals(itensPedido, List.of(itemPedido));

        verify(itemPedidoRepository).findAllById(List.of(1L));
        verifyNoMoreInteractions(itemPedidoRepository);
    }

    @Test
    public void deveLancarItemPedidoNaoEncontradoExceptionAoBuscarItemPedidoPorId() {
        Mockito.when(itemPedidoRepository.findById(2L)).thenReturn(Optional.empty());

        ItemPedidoNaoEncontradoException exception = Assertions.assertThrows(ItemPedidoNaoEncontradoException.class,
                () -> {itemPedidoService.buscarPorId(2L);
                });

        assertEquals("Não existe um cadastro de itemPedido com codigo 2", exception.getMessage());

        verify(itemPedidoRepository).findById(2L);
        verifyNoMoreInteractions(itemPedidoRepository);
    }



    @Test
    public void deveAdicionarNovaItemPedido() {
        Mockito.when(itemPedidoRepository.save(itemPedido)).thenReturn(itemPedido);
        Mockito.when(produtoService.buscarPorId(1L)).thenReturn(produto);

        ItemPedido iP = itemPedidoService.adicionar(itemPedido);

        assertEquals(iP, itemPedido);
        assertEquals(iP.getPrecoUnitario(), BigDecimal.valueOf(45));
        assertEquals(iP.getPrecoTotal(), BigDecimal.valueOf(90));
        verify(itemPedidoRepository).save(itemPedido);
        verifyNoMoreInteractions(itemPedidoRepository);
    }

    @Test
    public void deveExcluirItemPedido() {

        itemPedidoService.remover(1L);

        verify(itemPedidoRepository).deleteById(1L);
        verifyNoMoreInteractions(itemPedidoRepository);
    }

    @Test
    public void deveLancarItemPedidoNaoEncontradoExceptionAoExcluirItemPedidoComIdInexistente() {
        Mockito.doThrow(new EmptyResultDataAccessException(2)).when(itemPedidoRepository).deleteById(2L);

        ItemPedidoNaoEncontradoException exception = Assertions.assertThrows(ItemPedidoNaoEncontradoException.class,
                () -> {itemPedidoService.remover(2L);
                });

        assertEquals("Não existe um cadastro de itemPedido com codigo 2", exception.getMessage());

        verify(itemPedidoRepository).deleteById(2L);
        verifyNoMoreInteractions(itemPedidoRepository);
    }

    @Test
    public void deveLancarEntidadeEmUsoExceptionAoExcluirItemPedidoEmUso() {
        Mockito.doThrow(new DataIntegrityViolationException("")).when(itemPedidoRepository).deleteById(1L);

        EntidadeEmUsoException exception = Assertions.assertThrows(EntidadeEmUsoException.class,
                () -> {itemPedidoService.remover(1L);
                });

        assertEquals("ItemPedido de código 1 não pode ser removido, pois esta em uso", exception.getMessage());

        verify(itemPedidoRepository).deleteById(1L);
        verifyNoMoreInteractions(itemPedidoRepository);
    }

}