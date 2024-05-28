package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.exceptions.PermissaoNaoEncontradaException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Permissao;
import com.jeanpiress.ProjetoBarbearia.domain.model.Usuario;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PermissaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
class PermissaoServiceTest {

    @InjectMocks
    PermissaoService permissaoService;

    @Mock
    PermissaoRepository permissaoRepository;

    Permissao permissao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        permissao = new Permissao(1L, "GERENTE", null, Set.of(Usuario.builder().build()));
    }

    @Test
    public void deveBuscarPermissaoPorId() {
        Mockito.when(permissaoRepository.findById(1L)).thenReturn(Optional.of(permissao));

        Permissao permissaoSalvo = permissaoService.buscarPorId(1L);

        assertEquals(permissaoSalvo, permissaoSalvo);
        verify(permissaoRepository).findById(1L);
        verifyNoMoreInteractions(permissaoRepository);
    }

    @Test
    public void deveLancarPermissaoNaoEncontradoExceptionAoBuscarPermissaoPorId() {
        Mockito.when(permissaoRepository.findById(2L)).thenReturn(Optional.empty());

        PermissaoNaoEncontradaException exception = Assertions.assertThrows(PermissaoNaoEncontradaException.class,
                () -> {permissaoService.buscarPorId(2L);
                });

        assertEquals("Não existe um cadastro de permissao com codigo 2", exception.getMessage());

        verify(permissaoRepository).findById(2L);
        verifyNoMoreInteractions(permissaoRepository);
    }
}