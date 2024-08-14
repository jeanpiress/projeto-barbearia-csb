package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ClienteAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.ClienteInputDissembler;
import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbearia.domain.model.Endereco;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ClienteRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class ClienteControllerTest {

    @InjectMocks
    ClienteController clienteController;

    @Mock
    ClienteService clienteService;

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    ClienteAssembler clienteAssembler;

    @Mock
    ClienteInputDissembler clienteInputDissembler;

    MockMvc mockMvc;
    Cliente cliente;
    Endereco endereco;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).alwaysDo(print()).build();
        
        endereco = new Endereco("23456789", "Rua canção", "123", "ape 456", "morumbi");
        cliente = new Cliente(1L, "João", "34999999999", OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), null,
                BigDecimal.ZERO, null, null, 30, null, endereco);

    }

    @Test
    void deveListarTodosClientes() throws Exception {
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteRepository.findAll()).thenReturn(clientes);

        mockMvc.perform(get("/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(clienteRepository).findAll();
        verifyNoMoreInteractions(clienteRepository);
    }

    @Test
    void deveListarTodosClientesComNomeInformado() throws Exception {
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteRepository.findByNome("João")).thenReturn(clientes);

        mockMvc.perform(get("/clientes")
                        .param("nome", "João")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(clienteRepository).findByNome(any());
        verifyNoMoreInteractions(clienteRepository);
    }

    @Test
    void deveBuscarClientePorId() throws Exception {
        when(clienteService.buscarPorId(anyLong())).thenReturn(cliente);

        mockMvc.perform(get("/clientes/{clienteId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(clienteService).buscarPorId(1L);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void deveAdicionarUmNovoCliente() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(clienteService.adicionar(cliente)).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(clienteService).adicionar(any());
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void deveAlterarUmCliente() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(clienteService.buscarPorId(anyLong())).thenReturn(cliente);
        when(clienteService.adicionar(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(put("/clientes/{clienteId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(clienteService).buscarPorId(1L);
        verify(clienteService).adicionar(cliente);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void deveDeletarUmCliente() throws Exception {
        doNothing().when(clienteService).remover(anyLong());

        mockMvc.perform(delete("/clientes/{clienteId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(clienteService).remover(1L);
        verifyNoMoreInteractions(clienteService);
    }
}