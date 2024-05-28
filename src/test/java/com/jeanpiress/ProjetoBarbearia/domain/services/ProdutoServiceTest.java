package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.eventos.PacoteProntoCriadoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.eventos.ProdutoCriadoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ProdutoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ProdutoRepository;
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
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class ProdutoServiceTest {

    @Spy
    @InjectMocks
    ProdutoService produtoService;

    @Mock
    ProdutoRepository produtoRepository;

    @Mock
    CategoriaService categoriaService;

    @Mock
    ApplicationEventPublisher eventPublisher;

    Produto produto;
    Categoria categoria;
    PacotePronto pacotePronto;
    ItemPedido itemPedido;
    ItemPacote itemPacote;
    ItemPacote itemPacote2;
    PacoteProntoCriadoEvento pacoteProntoEvento;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        categoria = new Categoria(1L, "produtos");

        produto = new Produto(1L, "corte", BigDecimal.valueOf(45), true, false, null,
                false, BigDecimal.ONE, BigDecimal.ONE,null, BigDecimal.valueOf(50), categoria, null);

        itemPedido = new ItemPedido(1L, BigDecimal.valueOf(45), BigDecimal.valueOf(45), 1, null, produto);

        itemPacote = new ItemPacote(1L, itemPedido, Profissional.builder().build(), OffsetDateTime.now());
        itemPacote2 = new ItemPacote(2L, itemPedido, Profissional.builder().build(), OffsetDateTime.now());

        pacotePronto = new PacotePronto(1L, "2 barbas", null, 30, true, BigDecimal.valueOf(10), BigDecimal.valueOf(2), BigDecimal.valueOf(2),
                List.of(itemPacote, itemPacote2), categoria);

        pacoteProntoEvento = new PacoteProntoCriadoEvento(pacotePronto);
    }

    @Test
    public void deveBuscarProdutoPorId() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Produto produtoSalvo = produtoService.buscarPorId(1L);

        assertEquals(produtoSalvo, produto);
        verify(produtoRepository).findById(1L);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    public void deveLancarProdutoNaoEncontradoExceptionAoBuscarProdutoPorId() {
        when(produtoRepository.findById(2L)).thenReturn(Optional.empty());

        ProdutoNaoEncontradoException exception = Assertions.assertThrows(ProdutoNaoEncontradoException.class,
                () -> {produtoService.buscarPorId(2L);
                });

        assertEquals("Não existe um cadastro de produto com codigo 2", exception.getMessage());

        verify(produtoRepository).findById(2L);
        verifyNoMoreInteractions(produtoRepository);
    }



    @Test
    public void deveAdicionarNovaProduto() {
        when(produtoRepository.save(produto)).thenReturn(produto);
        when(categoriaService.buscarPorId(1L)).thenReturn(categoria);

        Produto produtoSalvo = produtoService.adicionar(produto);

        ArgumentCaptor<ProdutoCriadoEvento> eventCaptor = ArgumentCaptor.forClass(ProdutoCriadoEvento.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        ProdutoCriadoEvento capturedEvent = eventCaptor.getValue();

        assertEquals(capturedEvent.getProduto(), produto);
        assertEquals(produtoSalvo, produto);
        verify(produtoRepository).save(produto);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    public void deveExcluirProduto() {

        produtoService.remover(1L);

        verify(produtoRepository).deleteById(1L);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    public void deveLancarProdutoNaoEncontradoExceptionAoExcluirProdutoComIdInexistente() {
        doThrow(new EmptyResultDataAccessException(2)).when(produtoRepository).deleteById(2L);

        ProdutoNaoEncontradoException exception = Assertions.assertThrows(ProdutoNaoEncontradoException.class,
                () -> {produtoService.remover(2L);
                });

        assertEquals("Não existe um cadastro de produto com codigo 2", exception.getMessage());

        verify(produtoRepository).deleteById(2L);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    public void deveLancarEntidadeEmUsoExceptionAoExcluirProdutoEmUso() {
        doThrow(new DataIntegrityViolationException("")).when(produtoRepository).deleteById(1L);

        EntidadeEmUsoException exception = Assertions.assertThrows(EntidadeEmUsoException.class,
                () -> {produtoService.remover(1L);
                });

        assertEquals("Produto de código 1 não pode ser removido, pois esta em uso", exception.getMessage());

        verify(produtoRepository).deleteById(1L);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    public void transformaPacoteProntoEmProdutoNovo(){
        when(categoriaService.buscarPorId(1L)).thenReturn(categoria);

        produtoService.transformaPacoteProntoEmProdutoNovo(pacoteProntoEvento);

        ArgumentCaptor<Produto> produtoCaptor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoRepository).save(produtoCaptor.capture());
        Produto produtoSalvo = produtoCaptor.getValue();

        assertEquals(produtoSalvo.getPreco(), BigDecimal.valueOf(90));
        assertEquals(produtoSalvo.getComissaoBase(), BigDecimal.valueOf(10));

        verify(categoriaService).buscarPorId(1L);
        verify(produtoService).transformaPacoteProntoEmProdutoNovo(pacoteProntoEvento);
        verify(produtoRepository).save(produtoSalvo);
        verifyNoMoreInteractions(categoriaService, produtoService, produtoRepository);
    }
}