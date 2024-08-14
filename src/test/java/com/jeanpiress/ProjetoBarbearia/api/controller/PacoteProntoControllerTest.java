package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.PacoteProntoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.PacoteProntoInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.PacoteProntoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.PacoteProntoInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.model.PacotePronto;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PacoteProntoRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.PacoteProntoService;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class PacoteProntoControllerTest {

    @InjectMocks
    PacoteProntoController pacoteProntoController;

    @Mock
    PacoteProntoService pacoteProntoService;

    @Mock
    PacoteProntoRepository pacoteProntoRepository;

    @Mock
    PacoteProntoAssembler pacoteProntoAssembler;

    @Mock
    PacoteProntoInputDissembler pacoteProntoDissembler;

    MockMvc mockMvc;
    PacotePronto pacotePronto;
    PacoteProntoDto pacoteProntoDto;
    PacoteProntoInput pacoteProntoInput;
    ItemPacote itemPacote;
    List<ItemPacote> itensPacote;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pacoteProntoController).alwaysDo(print()).build();

        itemPacote = new ItemPacote(1L, null, null, null);
        itensPacote = List.of(itemPacote);
        pacotePronto = new PacotePronto(1L, "2 Barbas", null, 31, true, BigDecimal.valueOf(50), BigDecimal.ZERO, BigDecimal.ZERO,
                itensPacote, Categoria.builder().id(1L).build());
        pacoteProntoDto = new PacoteProntoDto();
        pacoteProntoInput = new PacoteProntoInput();
    }

    @Test
    void deveListarTodosPacotesProntos() throws Exception {
        List<PacotePronto> pacotesProntos = Arrays.asList(pacotePronto);

        when(pacoteProntoRepository.findAll()).thenReturn(pacotesProntos);
        when(pacoteProntoAssembler.collectionToModel(pacotesProntos)).thenReturn(Arrays.asList(pacoteProntoDto));

        mockMvc.perform(get("/pacotes-prontos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(pacoteProntoRepository).findAll();
        verifyNoMoreInteractions(pacoteProntoRepository);
    }

    @Test
    void deveListarPacotesProntosAtivos() throws Exception {
        List<PacotePronto> pacotesProntos = Arrays.asList(pacotePronto);

        when(pacoteProntoRepository.buscarPacoteProntoAtivo()).thenReturn(pacotesProntos);
        when(pacoteProntoAssembler.collectionToModel(pacotesProntos)).thenReturn(Arrays.asList(pacoteProntoDto));

        mockMvc.perform(get("/pacotes-prontos/ativo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(pacoteProntoRepository).buscarPacoteProntoAtivo();
        verifyNoMoreInteractions(pacoteProntoRepository);
    }

    @Test
    void deveBuscarPacoteProntoPorId() throws Exception {
        when(pacoteProntoService.buscarPorId(anyLong())).thenReturn(pacotePronto);
        when(pacoteProntoAssembler.toModel(any(PacotePronto.class))).thenReturn(pacoteProntoDto);

        mockMvc.perform(get("/pacotes-prontos/{pacoteProntoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(pacoteProntoService).buscarPorId(1L);
        verifyNoMoreInteractions(pacoteProntoService);
    }

    @Test
    void deveCriarUmNovoPacotePronto() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(pacoteProntoDissembler.toDomainObject(any(PacoteProntoInput.class))).thenReturn(pacotePronto);
        when(pacoteProntoService.criarPacotePronto(any(PacotePronto.class))).thenReturn(pacotePronto);
        when(pacoteProntoAssembler.toModel(any(PacotePronto.class))).thenReturn(pacoteProntoDto);

        mockMvc.perform(post("/pacotes-prontos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacotePronto)))
                .andExpect(status().isOk())
                .andReturn();

        verify(pacoteProntoDissembler).toDomainObject(any(PacoteProntoInput.class));
        verify(pacoteProntoService).criarPacotePronto(any(PacotePronto.class));
        verifyNoMoreInteractions(pacoteProntoService);
    }
}
