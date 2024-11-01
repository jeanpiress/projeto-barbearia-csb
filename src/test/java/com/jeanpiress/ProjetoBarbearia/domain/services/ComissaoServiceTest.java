package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.eventos.ProdutoCriadoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ComissaoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ComissaoRepository;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ProfissionalRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class ComissaoServiceTest {

    @InjectMocks
    ComissaoService comissaoService;

    @Mock
    ComissaoRepository comissaoRepository;

    @Mock
    ProdutoService produtoService;

    @Mock
    ProfissionalService profissionalService;

    @Mock
    ProfissionalRepository profissionalRepository;


    Comissao comissao;
    Produto produto;
    Produto produtoNovo;
    Profissional profissional;
    Profissional profissional2;
    ProdutoCriadoEvento produtoCriadoEvento;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        produto = new Produto(1L, "corte", BigDecimal.valueOf(45), true, false, null,
                false, BigDecimal.ONE, BigDecimal.ONE,null, BigDecimal.valueOf(50), Categoria.builder().build(), null);

        produtoNovo = new Produto(2L, "shampoo", BigDecimal.valueOf(60), true, true, 1,
                false, BigDecimal.ONE, BigDecimal.ONE,null, BigDecimal.valueOf(10), Categoria.builder().build(), null);

        profissional = new Profissional(1L, "Jean Pires", "Jean", "34999999999", null,
                OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), BigDecimal.ZERO,null, true, null);

        profissional2 = new Profissional();
        profissional2.setId(2L);

        comissao = new Comissao(1L, produto, profissional, BigDecimal.valueOf(50));

        produtoCriadoEvento = new ProdutoCriadoEvento(produtoNovo);

    }

    @Test
    public void deveBuscarComissaoPorId() {
        when(comissaoRepository.findById(1L)).thenReturn(Optional.of(comissao));

        Comissao comissaoSalva = comissaoService.buscarPorId(1L);

        assertEquals(comissaoSalva, comissao);
        assertEquals(comissaoSalva.getId(), 1L);
        verify(comissaoRepository).findById(1L);
        verifyNoMoreInteractions(comissaoRepository);
    }

    @Test
    public void deveLancarComissaoNaoEncontradoExceptionAoBuscarComissaoPorId() {
        when(comissaoRepository.findById(2L)).thenReturn(Optional.empty());

        ComissaoNaoEncontradoException exception = assertThrows(ComissaoNaoEncontradoException.class,
                () -> {comissaoService.buscarPorId(2L);
                });

        assertEquals("Não existe um cadastro de comissao com codigo 2", exception.getMessage());

        verify(comissaoRepository).findById(2L);
        verifyNoMoreInteractions(comissaoRepository);
    }



    @Test
    public void deveAdicionarNovaComissao() {
        when(produtoService.buscarPorId(1L)).thenReturn(produto);
        when(profissionalService.buscarPorId(1L)).thenReturn(profissional);
        when(comissaoRepository.save(comissao)).thenReturn(comissao);

        Comissao comi = comissaoService.adicionar(comissao);

        assertEquals(comi, comissao);
        verify(comissaoRepository).save(comissao);
        verifyNoMoreInteractions(comissaoRepository);
        verify(produtoService).buscarPorId(1L);
        verifyNoMoreInteractions(produtoService);
        verify(profissionalService).buscarPorId(1L);
        verifyNoMoreInteractions(profissionalService);
    }

    @Test
    public void deveExcluirComissao() {

        comissaoService.remover(1L);

        verify(comissaoRepository).deleteById(1L);
        verifyNoMoreInteractions(comissaoRepository);

    }

    @Test
    public void deveLancarComissaoNaoEncontradoExceptionAoExcluirComissaoComIdInexistente() {
        Mockito.doThrow(new EmptyResultDataAccessException(2)).when(comissaoRepository).deleteById(2L);

        ComissaoNaoEncontradoException exception = assertThrows(ComissaoNaoEncontradoException.class,
                () -> {comissaoService.remover(2L);
                });

        assertEquals("Não existe um cadastro de comissao com codigo 2", exception.getMessage());

        verify(comissaoRepository).deleteById(2L);
        verifyNoMoreInteractions(comissaoRepository);
    }

    @Test
    public void deveLancarEntidadeEmUsoExceptionAoExcluirComissaoEmUso() {
        Mockito.doThrow(new DataIntegrityViolationException("")).when(comissaoRepository).deleteById(1L);

        EntidadeEmUsoException exception = assertThrows(EntidadeEmUsoException.class,
                () -> {comissaoService.remover(1L);
                });

        assertEquals("Comissao de código 1 não pode ser removido, pois esta em uso", exception.getMessage());

        verify(comissaoRepository).deleteById(1L);
        verifyNoMoreInteractions(comissaoRepository);
    }

    @Test
    public void deveBuscarUmaComissaoPassandoProfissionalIdProdutoId(){
        when(comissaoRepository.buscarPorProfissionalEProduto(1L, 1L)).thenReturn(Optional.of(comissao));
        Comissao comi = comissaoService.buscarPorProfissionalProduto(1L, 1L);

        Assertions.assertEquals(comi, comissao);

        verify(comissaoRepository).buscarPorProfissionalEProduto(1L, 1L);
        verifyNoMoreInteractions(comissaoRepository);

    }

    @Test
    public void deveLancarComissaoNaoEncontradaExcepitionAoPassarProfissionalIdOuProdutoIdInvalido(){
        when(comissaoRepository.buscarPorProfissionalEProduto(2L, 2L)).thenReturn(Optional.empty());

        ComissaoNaoEncontradoException exception = assertThrows(ComissaoNaoEncontradoException.class,
                () -> {comissaoService.buscarPorProfissionalProduto(2L, 2L);
                });

        assertEquals("Comissao não foi encontrada", exception.getMessage());

        verify(comissaoRepository).buscarPorProfissionalEProduto(2L, 2L);
        verifyNoMoreInteractions(comissaoRepository);
    }

    @Test
    public void deveCalcularValorComissaoProduto(){

        BigDecimal comissaoCalculada =  comissaoService.calculoComissaoProduto(comissao);

        Assertions.assertEquals(comissaoCalculada, BigDecimal.valueOf(22.5));

        verifyNoMoreInteractions(comissaoRepository);
    }

    @Test
    public void deveCriarAComissaoBaseDoProdutoParaTodosProfissionais() {
        when(profissionalRepository.buscarProfissionaisAtivos()).thenReturn(List.of(profissional));

        comissaoService.criarComissaoBase(produtoCriadoEvento);

        ArgumentCaptor<List<Comissao>> comissaoCaptor = ArgumentCaptor.forClass(List.class);
        verify(comissaoRepository).saveAll(comissaoCaptor.capture());
        verifyNoMoreInteractions(comissaoRepository);

        verify(profissionalRepository).buscarProfissionaisAtivos();
        verifyNoMoreInteractions(profissionalRepository);

        List<Comissao> comissoesCapturadas = comissaoCaptor.getValue();
        Comissao comissao = comissoesCapturadas.get(0);

        assertEquals(Set.of(profissional).size(), comissoesCapturadas.size());
        assertEquals(produtoNovo, comissao.getProduto());
        assertTrue(Set.of(profissional).contains(comissao.getProfissional()));
        assertEquals(produtoNovo.getComissaoBase(), comissao.getPorcentagemComissao());

    }

}