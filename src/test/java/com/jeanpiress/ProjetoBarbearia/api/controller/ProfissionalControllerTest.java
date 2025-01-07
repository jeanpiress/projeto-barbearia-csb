package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ProfissionalAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.ProfissionalInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ProfissionalDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ProfissionalInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ProfissionalRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.ProfissionalService;
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
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class ProfissionalControllerTest {

    @InjectMocks
    ProfissionalController profissionalController;

    @Mock
    ProfissionalService profissionalService;

    @Mock
    ProfissionalRepository profissionalRepository;

    @Mock
    ProfissionalAssembler profissionalAssembler;

    @Mock
    ProfissionalInputDissembler profissionalInputDissembler;

    MockMvc mockMvc;
    Profissional profissional;
    ProfissionalDto profissionalDto;
    ProfissionalInput profissionalInput;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(profissionalController).alwaysDo(print()).build();

        profissional = new Profissional(1L, "João Silva", "João", "34999999999",
                null, LocalDate.parse("1991-11-13"), BigDecimal.ZERO, null, true, null);

        profissionalDto = new ProfissionalDto();
        profissionalInput = new ProfissionalInput();
    }

    @Test
    void deveListarTodosProfissionais() throws Exception {
        List<Profissional> profissionais = Arrays.asList(profissional);

        when(profissionalRepository.findAll()).thenReturn(profissionais);
        when(profissionalAssembler.collectionToModel(profissionais)).thenReturn(Arrays.asList(profissionalDto));

        mockMvc.perform(get("/profissionais")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(profissionalRepository).findAll();
        verifyNoMoreInteractions(profissionalRepository);
    }

    @Test
    void deveBuscarProfissionalPorId() throws Exception {
        when(profissionalService.buscarPorId(anyLong())).thenReturn(profissional);
        when(profissionalAssembler.toModel(any(Profissional.class))).thenReturn(profissionalDto);

        mockMvc.perform(get("/profissionais/{profissionalId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(profissionalService).buscarPorId(1L);
        verifyNoMoreInteractions(profissionalService);
    }

    @Test
    void deveAdicionarUmNovoProfissional() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(profissionalService.adicionar(any(Profissional.class))).thenReturn(profissional);

        mockMvc.perform(post("/profissionais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profissional)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(profissionalService).adicionar(any());
        verifyNoMoreInteractions(profissionalService);
    }

    @Test
    void deveAlterarUmProfissional() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(profissionalService.buscarPorId(anyLong())).thenReturn(profissional);
        doNothing().when(profissionalInputDissembler).copyToDomainObject(any(ProfissionalInput.class), any(Profissional.class));
        when(profissionalService.adicionar(any(Profissional.class))).thenReturn(profissional);
        when(profissionalAssembler.toModel(any(Profissional.class))).thenReturn(profissionalDto);

        mockMvc.perform(put("/profissionais/{profissionalId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profissional)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(profissionalService).buscarPorId(1L);
        verify(profissionalService).adicionar(any(Profissional.class));
        verifyNoMoreInteractions(profissionalService);
    }

    @Test
    void deveDeletarUmProfissional() throws Exception {
        doNothing().when(profissionalService).remover(anyLong());

        mockMvc.perform(delete("/profissionais/{profissionalId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(profissionalService).remover(1L);
        verifyNoMoreInteractions(profissionalService);
    }
}
