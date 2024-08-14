package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.CategoriaAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.CategoriaInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.CategoriaDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.CategoriaInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.CategoriaRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
class CategoriaControllerTest {

    @InjectMocks
    CategoriaController categoriaController;

    @Mock
    CategoriaService categoriaService;

    @Mock
    CategoriaRepository categoriaRepository;

    @Mock
    CategoriaAssembler categoriaAssembler;

    @Mock
    CategoriaInputDissembler categoriaInputDissembler;

    MockMvc mockMvc;
    Categoria categoria;
    CategoriaDto categoriaDto;
    CategoriaInput categoriaInput;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).alwaysDo(print()).build();

        categoria = new Categoria(1L, "produtos");
        categoriaDto = new CategoriaDto(1L, "produtos");
        categoriaInput = new CategoriaInput("produtos");
    }

    @Test
    void deveListarTodasCategorias() throws Exception {
        List<Categoria> categorias = Arrays.asList(categoria);
        when(categoriaRepository.findAll()).thenReturn(categorias);
        
        mockMvc.perform(get("/categorias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(categoriaRepository).findAll();
        verifyNoMoreInteractions(categoriaRepository);
    }

    @Test
    void deveBuscarCategoriaPorId() throws Exception {
        when(categoriaService.buscarPorId(anyLong())).thenReturn(categoria);

        mockMvc.perform(get("/categorias/{categoriaId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(categoriaService).buscarPorId(1L);
        verifyNoMoreInteractions(categoriaService);
    }

    @Test
    void deveAdicionarUmaNovaCategoria() throws Exception {
        when(categoriaService.adicionar(any())).thenReturn(categoria);

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoriaInput)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(categoriaService).adicionar(any());
        verifyNoMoreInteractions(categoriaService);

    }

    @Test
    void deveAlterarUmaCategoria() throws Exception {
        when(categoriaService.buscarPorId(anyLong())).thenReturn(categoria);
        when(categoriaService.adicionar(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(put("/categorias/{categoriaId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoriaInput)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(categoriaService).buscarPorId(1L);
        verify(categoriaService).adicionar(categoria);
        verifyNoMoreInteractions(categoriaService);
        
    }

    @Test
    void deveDarErroAoAlterarUmaCategoria()throws Exception{
        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        verifyNoInteractions(categoriaService);
    }

    @Test
    void deveDeletarUmaCategoria() throws Exception {
        doNothing().when(categoriaService).remover(anyLong());

        mockMvc.perform(delete("/categorias/{categoriaId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoriaService).remover(1L);
        verifyNoMoreInteractions(categoriaService);
    }
}