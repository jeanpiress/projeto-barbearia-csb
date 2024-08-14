package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.PacoteAssembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.PacoteDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ItemPacoteId;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.PacoteId;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalId;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.RealizacaoItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.model.Pacote;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PacoteRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.PacoteService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class PacoteControllerTest {

    @InjectMocks
    PacoteController pacoteController;

    @Mock
    PacoteService pacoteService;

    @Mock
    PacoteRepository pacoteRepository;

    @Mock
    PacoteAssembler pacoteAssembler;

    MockMvc mockMvc;
    Pacote pacote;
    PacoteDto pacoteDto;
    RealizacaoItemPacote realizacaoItemPacote;
    Cliente cliente;
    Profissional profissional;
    ItemPacote itemPacote;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pacoteController).alwaysDo(print()).build();

        profissional = new Profissional(1L, "João Silva", "João", "34999999999", null,
                OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), BigDecimal.ZERO, null, true, null);

        itemPacote = new ItemPacote(1L, null, profissional, null);

        cliente = new Cliente(1L, "João", "34999999999", OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), null,
                BigDecimal.ZERO, null, null, 30, profissional, null);

        pacote = new Pacote(1L, cliente, OffsetDateTime.parse("2024-05-22T13:00:00-03:00"), 30,
                OffsetDateTime.parse("2024-06-22T13:00:00-03:00"), "2 barbas", null, List.of(itemPacote),
                List.of(), List.of());

        realizacaoItemPacote = new RealizacaoItemPacote(ProfissionalId.builder().id(1L).build(), PacoteId.builder().id(1L).build(),
                ItemPacoteId.builder().id(1L).build());

    }

    @Test
    void deveListarTodosPacotes() throws Exception {
        List<Pacote> pacotes = Arrays.asList(pacote);

        when(pacoteRepository.findAll()).thenReturn(pacotes);
        when(pacoteAssembler.collectionToModel(pacotes)).thenReturn(Arrays.asList(pacoteDto));

        mockMvc.perform(get("/pacotes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(pacoteRepository).findAll();
        verifyNoMoreInteractions(pacoteRepository);
    }

    @Test
    void deveBuscarPacotePorId() throws Exception {
        when(pacoteService.buscarPorId(anyLong())).thenReturn(pacote);
        when(pacoteAssembler.toModel(any(Pacote.class))).thenReturn(pacoteDto);

        mockMvc.perform(get("/pacotes/{pacoteId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(pacoteService).buscarPorId(1L);
        verifyNoMoreInteractions(pacoteService);
    }

    @Test
    void deveBuscarPacotesAtivos() throws Exception {
        List<Pacote> pacotes = Arrays.asList(pacote);

        when(pacoteService.buscarPacotesComItensAtivos()).thenReturn(pacotes);
        when(pacoteAssembler.collectionToModel(pacotes)).thenReturn(Arrays.asList(pacoteDto));

        mockMvc.perform(get("/pacotes/ativos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(pacoteService).buscarPacotesComItensAtivos();
        verifyNoMoreInteractions(pacoteService);
    }

    @Test
    void deveBuscarPacotesExpirados() throws Exception {
        List<Pacote> pacotes = Arrays.asList(pacote);

        when(pacoteService.buscarPacotesComItensExpirados()).thenReturn(pacotes);
        when(pacoteAssembler.collectionToModel(pacotes)).thenReturn(Arrays.asList(pacoteDto));

        mockMvc.perform(get("/pacotes/expirados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(pacoteService).buscarPacotesComItensExpirados();
        verifyNoMoreInteractions(pacoteService);
    }

    @Test
    void deveBuscarPacotesPorCliente() throws Exception {
        List<Pacote> pacotes = Arrays.asList(pacote);

        when(pacoteService.buscarPorClinte(anyLong())).thenReturn(pacotes);
        when(pacoteAssembler.collectionToModel(pacotes)).thenReturn(Arrays.asList(pacoteDto));

        mockMvc.perform(get("/pacotes/cliente/{clienteId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(pacoteService).buscarPorClinte(1L);
        verifyNoMoreInteractions(pacoteService);
    }

    @Test
    void deveReceberPacote() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(pacoteService.realizarUmItemDoPacote(any(RealizacaoItemPacote.class))).thenReturn(pacote);
        when(pacoteAssembler.toModel(any(Pacote.class))).thenReturn(pacoteDto);

        mockMvc.perform(put("/pacotes/receber-pacote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(realizacaoItemPacote)))
                .andExpect(status().isOk())
                .andReturn();

        verify(pacoteService).realizarUmItemDoPacote(any(RealizacaoItemPacote.class));
        verifyNoMoreInteractions(pacoteService);
    }
}
