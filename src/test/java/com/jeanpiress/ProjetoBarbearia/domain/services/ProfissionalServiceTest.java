package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ProfissionalNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ProfissionalRepository;
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
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
class ProfissionalServiceTest {

    @InjectMocks
    ProfissionalService profissionalService;

    @Mock
    ProfissionalRepository profissionalRepository;

    Profissional profissional;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        profissional = new Profissional(1L, "João Silva", "João", "34999999999", null,
                OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), BigDecimal.ZERO, null, true, null);

    }

    @Test
    public void deveBuscarProfissionalPorId() {
        Mockito.when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));

        Profissional profissionalSalvo = profissionalService.buscarPorId(1L);

        assertEquals(profissionalSalvo, profissional);
        verify(profissionalRepository).findById(1L);
        verifyNoMoreInteractions(profissionalRepository);
    }

    @Test
    public void deveLancarProfissionalNaoEncontradoExceptionAoBuscarProfissionalPorId() {
        Mockito.when(profissionalRepository.findById(2L)).thenReturn(Optional.empty());

        ProfissionalNaoEncontradoException exception = Assertions.assertThrows(ProfissionalNaoEncontradoException.class,
                () -> {profissionalService.buscarPorId(2L);
                });

        assertEquals("Não existe um cadastro de profissional com codigo 2", exception.getMessage());

        verify(profissionalRepository).findById(2L);
        verifyNoMoreInteractions(profissionalRepository);
    }



    @Test
    public void deveAdicionarNovaProfissional() {
        Mockito.when(profissionalRepository.save(profissional)).thenReturn(profissional);

        Profissional profissionalSalvo = profissionalService.adicionar(profissional);

        assertEquals(profissionalSalvo, profissional);
        verify(profissionalRepository).save(profissional);
        verifyNoMoreInteractions(profissionalRepository);
    }

    @Test
    public void deveExcluirProfissional() {

        profissionalService.remover(1L);

        verify(profissionalRepository).deleteById(1L);
        verifyNoMoreInteractions(profissionalRepository);
    }

    @Test
    public void deveLancarProfissionalNaoEncontradoExceptionAoExcluirProfissionalComIdInexistente() {
        Mockito.doThrow(new EmptyResultDataAccessException(2)).when(profissionalRepository).deleteById(2L);

        ProfissionalNaoEncontradoException exception = Assertions.assertThrows(ProfissionalNaoEncontradoException.class,
                () -> {profissionalService.remover(2L);
                });

        assertEquals("Não existe um cadastro de profissional com codigo 2", exception.getMessage());

        verify(profissionalRepository).deleteById(2L);
        verifyNoMoreInteractions(profissionalRepository);
    }

    @Test
    public void deveLancarEntidadeEmUsoExceptionAoExcluirProfissionalEmUso() {
        Mockito.doThrow(new DataIntegrityViolationException("")).when(profissionalRepository).deleteById(1L);

        EntidadeEmUsoException exception = Assertions.assertThrows(EntidadeEmUsoException.class,
                () -> {profissionalService.remover(1L);
                });

        assertEquals("Profissional de código 1 não pode ser removido, pois esta em uso", exception.getMessage());

        verify(profissionalRepository).deleteById(1L);
        verifyNoMoreInteractions(profissionalRepository);
    }

}