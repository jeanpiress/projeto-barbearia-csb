package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ClienteAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.ClienteInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ClienteDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ClienteInput;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.CampoObrigatorioException;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
    ClienteInputDissembler clienteDissembler;

    MockMvc mockMvc;
    Cliente cliente;
    ClienteDto clienteDto;
    Endereco endereco;
    ClienteInput clienteInput;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).alwaysDo(print()).build();
        
        endereco = new Endereco("23456789", "Rua canção", "123", "ape 456",
                "morumbi", "Uberlandia", "Minas Gerais");

        cliente = new Cliente(1L, "João", "34999999999", LocalDate.parse("1991-11-13"),
                null, BigDecimal.ZERO, null, null, 30, true, null, endereco);

        clienteDto = new ClienteDto(1L, "João", "34999999999", LocalDate.parse("1991-11-13"),
                null, 50, null, null, 30, null, endereco );

        clienteInput = new ClienteInput("João", "34999999999", LocalDate.parse("1991-11-13"),
                null, 30, endereco );
    }

    @Test
    void deveListarClientesComParametrosValidos() throws Exception {
        List<Cliente> clientes = Arrays.asList(cliente);

        String nome = "Cliente Teste";
        boolean ativo = true;

        when(clienteRepository.findByNome(nome, ativo)).thenReturn(clientes);
        when(clienteAssembler.collectionToModel(clientes)).thenReturn(Arrays.asList(clienteDto));

        mockMvc.perform(get("/clientes")
                        .param("nome", nome)
                        .param("ativo", String.valueOf(ativo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(clienteRepository).findByNome(nome, ativo);
        verify(clienteAssembler).collectionToModel(clientes);
        verifyNoMoreInteractions(clienteRepository, clienteAssembler);
    }

    @Test
    void deveRetornarErroQuandoNomeNaoForInformado() throws Exception {
        boolean ativo = true;

        mockMvc.perform(get("/clientes")
                        .param("ativo", String.valueOf(ativo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


        mockMvc.perform(get("/clientes")
                        .param("nome", " ")
                        .param("ativo", String.valueOf(ativo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CampoObrigatorioException))
                .andExpect(result -> assertEquals("Nome é obrigatorio", result.getResolvedException().getMessage()));

        verifyNoInteractions(clienteRepository, clienteAssembler);
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

        when(clienteDissembler.toDomainObject(any(ClienteInput.class))).thenReturn(cliente);
        when(clienteService.adicionar(any(Cliente.class))).thenReturn(cliente);
        when(clienteAssembler.toModel(any(Cliente.class))).thenReturn(clienteDto);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteInput)))
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