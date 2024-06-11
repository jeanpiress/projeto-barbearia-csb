package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.eventos.ClienteAtendidoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ClienteNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbearia.domain.model.Endereco;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ClienteRepository;
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
class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    Cliente cliente;
    Endereco endereco;

    ClienteAtendidoEvento clienteAtendidoEvento;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        endereco = new Endereco("23456789", "Rua canção", "123", "ape 456", "morumbi");
        cliente = new Cliente(1L, "João", "34999999999", OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), null,
                BigDecimal.ZERO, null, null, 30, Profissional.builder().build(), endereco);

        clienteAtendidoEvento = new ClienteAtendidoEvento(cliente);

    }

    @Test
    public void deveBuscarClientePorId() {
        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente clienteSalvo = clienteService.buscarPorId(1L);

        assertEquals(clienteSalvo, cliente);
        assertEquals(clienteSalvo.getEndereco(), endereco);
        verify(clienteRepository).findById(1L);
        verifyNoMoreInteractions(clienteRepository);
    }

    @Test
    public void deveLancarClienteNaoEncontradoExceptionAoBuscarClientePorId() {
        Mockito.when(clienteRepository.findById(2L)).thenReturn(Optional.empty());

        ClienteNaoEncontradoException exception = Assertions.assertThrows(ClienteNaoEncontradoException.class,
                () -> {clienteService.buscarPorId(2L);
                });

        assertEquals("Não existe um cadastro de cliente com codigo 2", exception.getMessage());

        verify(clienteRepository).findById(2L);
        verifyNoMoreInteractions(clienteRepository);
    }

    @Test
    public void deveAdicionarNovoCliente() {
        Mockito.when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente cate = clienteService.adicionar(cliente);

        assertEquals(cate, cliente);
        verify(clienteRepository).save(cliente);
        verifyNoMoreInteractions(clienteRepository);
    }

    @Test
    public void deveExcluirCliente() {

        clienteService.remover(1L);

        verify(clienteRepository).deleteById(1L);
        verifyNoMoreInteractions(clienteRepository);
    }

    @Test
    public void deveLancarClienteNaoEncontradoExceptionAoExcluirClienteComIdInexistente() {
        Mockito.doThrow(new EmptyResultDataAccessException(2)).when(clienteRepository).deleteById(2L);

        ClienteNaoEncontradoException exception = Assertions.assertThrows(ClienteNaoEncontradoException.class,
                () -> {clienteService.remover(2L);
                });

        assertEquals("Não existe um cadastro de cliente com codigo 2", exception.getMessage());

        verify(clienteRepository).deleteById(2L);
        verifyNoMoreInteractions(clienteRepository);
    }

    @Test
    public void deveLancarEntidadeEmUsoExceptionAoExcluirClienteEmUso() {
        Mockito.doThrow(new DataIntegrityViolationException("")).when(clienteRepository).deleteById(1L);

        EntidadeEmUsoException exception = Assertions.assertThrows(EntidadeEmUsoException.class,
                () -> {clienteService.remover(1L);
                });

        assertEquals("Cliente de código 1 não pode ser removido, pois esta em uso", exception.getMessage());

        verify(clienteRepository).deleteById(1L);
        verifyNoMoreInteractions(clienteRepository);
    }

    @Test
    public void deveAlterarPrevisaoRetorno(){
        clienteService.alterarPrevisaoRetorno(clienteAtendidoEvento);

        assertEquals(cliente.getPrevisaoRetorno().getDayOfMonth(), OffsetDateTime.now().plusDays(30).getDayOfMonth());
        assertEquals(cliente.getPrevisaoRetorno().getMonth(), OffsetDateTime.now().plusDays(30).getMonth());

        verify(clienteRepository).save(cliente);
        verifyNoMoreInteractions(clienteRepository);
    }
}