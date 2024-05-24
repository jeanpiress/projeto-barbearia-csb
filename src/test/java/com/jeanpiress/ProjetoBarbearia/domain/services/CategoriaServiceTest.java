package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.exceptions.CategoriaNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.CategoriaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
class CategoriaServiceTest {

    @InjectMocks
    CategoriaService categoriaService;

    @Mock
    CategoriaRepository categoriaRepository;

    Categoria categoria;

    Categoria categoriaAlterada;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        categoria = new Categoria(1L, "produtos");
        categoriaAlterada = new Categoria(null, "serviço");

    }

    @Test
    public void deveBuscarCategoriaPorId() {
        Mockito.when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria cate = categoriaService.buscarPorId(1L);

        assertEquals(cate, categoria);
        verify(categoriaRepository).findById(1L);
        verifyNoMoreInteractions(categoriaRepository);
    }

    @Test
    public void deveLancarCategoriaNaoEncontradoExceptionAoBuscarCategoriaPorId() {
        Mockito.when(categoriaRepository.findById(2L)).thenReturn(Optional.empty());

        CategoriaNaoEncontradoException exception = Assertions.assertThrows(CategoriaNaoEncontradoException.class,
                () -> {categoriaService.buscarPorId(2L);
        });

        assertEquals("Não existe um cadastro de categoria com codigo 2", exception.getMessage());

        verify(categoriaRepository).findById(2L);
        verifyNoMoreInteractions(categoriaRepository);
    }



    @Test
    public void deveAdicionarNovaCategoria() {
        Mockito.when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria cate = categoriaService.adicionar(categoria);

        assertEquals(cate, categoria);
        verify(categoriaRepository).save(categoria);
        verifyNoMoreInteractions(categoriaRepository);
    }

    @Test
    public void deveExcluirCategoria() {

        categoriaService.remover(1L);

        verify(categoriaRepository).deleteById(1L);
        verifyNoMoreInteractions(categoriaRepository);
    }

    @Test
    public void deveLancarCategoriaNaoEncontradoExceptionAoExcluirCategoriaComIdInexistente() {
        Mockito.doThrow(new EmptyResultDataAccessException(2)).when(categoriaRepository).deleteById(2L);

        CategoriaNaoEncontradoException exception = Assertions.assertThrows(CategoriaNaoEncontradoException.class,
                () -> {categoriaService.remover(2L);
        });

        assertEquals("Não existe um cadastro de categoria com codigo 2", exception.getMessage());

        verify(categoriaRepository).deleteById(2L);
        verifyNoMoreInteractions(categoriaRepository);
    }

    @Test
    public void deveLancarEntidadeEmUsoExceptionAoExcluirCategoriaEmUso() {
        Mockito.doThrow(new DataIntegrityViolationException("")).when(categoriaRepository).deleteById(1L);

        EntidadeEmUsoException exception = Assertions.assertThrows(EntidadeEmUsoException.class,
                () -> {categoriaService.remover(1L);
                });

        assertEquals("Categoria de código 1 não pode ser removido, pois esta em uso", exception.getMessage());

        verify(categoriaRepository).deleteById(1L);
        verifyNoMoreInteractions(categoriaRepository);
    }


}