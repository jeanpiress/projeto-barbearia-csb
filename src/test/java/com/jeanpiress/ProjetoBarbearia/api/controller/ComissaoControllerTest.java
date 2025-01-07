package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ComissaoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.ComissaoInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ComissaoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ComissaoInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbearia.domain.model.Comissao;
import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ComissaoRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.ComissaoService;
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
class ComissaoControllerTest {

    @InjectMocks
    ComissaoController comissaoController;

    @Mock
    ComissaoService comissaoService;

    @Mock
    ComissaoRepository comissaoRepository;

    @Mock
    ComissaoAssembler comissaoAssembler;

    @Mock
    ComissaoInputDissembler comissaoInputDissembler;

    MockMvc mockMvc;
    Comissao comissao;
    Produto produto;
    Profissional profissional;
    ComissaoDto comissaoDto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(comissaoController).alwaysDo(print()).build();

        produto = new Produto(1L, "corte", BigDecimal.valueOf(45), true, false, null,
                false, BigDecimal.ONE, BigDecimal.ONE, null, BigDecimal.valueOf(50), Categoria.builder().build(), null);

        profissional = new Profissional(1L, "João Silva", "João", "34999999999",
                null, LocalDate.parse("1991-11-13"), BigDecimal.ZERO, null, true, null);

        comissao = new Comissao(1L, produto, profissional, BigDecimal.valueOf(50));
        comissaoDto = new ComissaoDto();
    }

    @Test
    void deveListarTodasComissoes() throws Exception {
        List<Comissao> comissoes = Arrays.asList(comissao);

        when(comissaoRepository.findAll()).thenReturn(comissoes);
        when(comissaoAssembler.collectionToModel(comissoes)).thenReturn(Arrays.asList(comissaoDto));

        mockMvc.perform(get("/comissoes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(comissaoRepository).findAll();
        verifyNoMoreInteractions(comissaoRepository);
    }

    @Test
    void deveBuscarComissaoPorId() throws Exception {
        when(comissaoService.buscarPorId(anyLong())).thenReturn(comissao);
        when(comissaoAssembler.toModel(any(Comissao.class))).thenReturn(comissaoDto);

        mockMvc.perform(get("/comissoes/{comissaoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(comissaoService).buscarPorId(1L);
        verifyNoMoreInteractions(comissaoService);
    }

    @Test
    void deveAdicionarUmaNovaComissao() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(comissaoInputDissembler.toDomainObject(any(ComissaoInput.class))).thenReturn(comissao);
        when(comissaoService.adicionar(any())).thenReturn(comissao);
        when(comissaoAssembler.toModel(any(Comissao.class))).thenReturn(comissaoDto);

        mockMvc.perform(post("/comissoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comissao)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(comissaoService).adicionar(comissao);
        verifyNoMoreInteractions(comissaoService);
    }

    @Test
    void deveAlterarUmaComissao() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(comissaoService.buscarPorId(anyLong())).thenReturn(comissao);
        doNothing().when(comissaoInputDissembler).copyToDomainObject(any(ComissaoInput.class), any(Comissao.class));
        when(comissaoService.adicionar(any(Comissao.class))).thenReturn(comissao);
        when(comissaoAssembler.toModel(any(Comissao.class))).thenReturn(comissaoDto);

        mockMvc.perform(put("/comissoes/{comissaoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comissao)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(comissaoService).buscarPorId(1L);
        verify(comissaoService).adicionar(comissao);
        verifyNoMoreInteractions(comissaoService);
    }

    @Test
    void deveDeletarUmaComissao() throws Exception {
        doNothing().when(comissaoService).remover(anyLong());

        mockMvc.perform(delete("/comissoes/{comissaoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(comissaoService).remover(1L);
        verifyNoMoreInteractions(comissaoService);
    }
}
