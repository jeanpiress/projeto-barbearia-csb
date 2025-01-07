package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ProdutoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.ProdutoInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ProdutoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ProdutoInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ProdutoRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.ProdutoService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class ProdutoControllerTest {

    @InjectMocks
    ProdutoController produtoController;

    @Mock
    ProdutoService produtoService;

    @Mock
    ProdutoRepository produtoRepository;

    @Mock
    ProdutoAssembler produtoAssembler;

    @Mock
    ProdutoInputDissembler produtoInputDissembler;

    MockMvc mockMvc;
    Produto produto;
    ProdutoDto produtoDto;
    ProdutoInput produtoInput;
    Categoria categoria;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController).alwaysDo(print()).build();

        categoria = new Categoria(1L, "produtos");
        produto = new Produto(1L, "corte", BigDecimal.valueOf(45), true, false, null, false, BigDecimal.ONE, BigDecimal.ONE, null, BigDecimal.valueOf(50), categoria, null);
        produtoDto = new ProdutoDto();
        produtoInput = new ProdutoInput();
    }

    @Test
    void deveListarProdutosComParametros() throws Exception {
        // Arrange
        List<Produto> produtos = Arrays.asList(produto);

        String nome = "Produto Teste";
        boolean isAtivo = true;
        Long categoriaId = 1L;

        when(produtoRepository.findByNome(nome, isAtivo, categoriaId)).thenReturn(produtos);
        when(produtoAssembler.collectionToModel(produtos)).thenReturn(Arrays.asList(produtoDto));

        mockMvc.perform(get("/produtos")
                        .param("nome", nome)
                        .param("isAtivo", String.valueOf(isAtivo))
                        .param("categoriaId", String.valueOf(categoriaId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(produtoRepository).findByNome(nome, isAtivo, categoriaId);
        verify(produtoAssembler).collectionToModel(produtos);
        verifyNoMoreInteractions(produtoRepository, produtoAssembler);
    }

    @Test
    void deveListarProdutosSemCategoriaId() throws Exception {
        List<Produto> produtos = Arrays.asList(produto);

        String nome = "Produto Teste";
        boolean isAtivo = true;

        when(produtoRepository.findByNome(nome, isAtivo, null)).thenReturn(produtos);
        when(produtoAssembler.collectionToModel(produtos)).thenReturn(Arrays.asList(produtoDto));

        mockMvc.perform(get("/produtos")
                        .param("nome", nome)
                        .param("isAtivo", String.valueOf(isAtivo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(produtoRepository).findByNome(nome, isAtivo, null);
        verify(produtoAssembler).collectionToModel(produtos);
        verifyNoMoreInteractions(produtoRepository, produtoAssembler);
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {
        when(produtoService.buscarPorId(anyLong())).thenReturn(produto);
        when(produtoAssembler.toModel(any(Produto.class))).thenReturn(produtoDto);

        mockMvc.perform(get("/produtos/{produtoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(produtoService).buscarPorId(1L);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void deveAdicionarUmNovoProduto() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(produtoService.adicionar(any(Produto.class))).thenReturn(produto);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(produtoService).adicionar(any());
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void deveAlterarUmProduto() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(produtoService.buscarPorId(anyLong())).thenReturn(produto);
        doNothing().when(produtoInputDissembler).copyToDomainObject(any(ProdutoInput.class), any(Produto.class));
        when(produtoService.adicionar(any(Produto.class))).thenReturn(produto);
        when(produtoAssembler.toModel(any(Produto.class))).thenReturn(produtoDto);

        mockMvc.perform(put("/produtos/{produtoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(produtoService).buscarPorId(1L);
        verify(produtoService).adicionar(any(Produto.class));
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void deveDeletarUmProduto() throws Exception {
        doNothing().when(produtoService).remover(anyLong());

        mockMvc.perform(delete("/produtos/{produtoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(produtoService).remover(1L);
        verifyNoMoreInteractions(produtoService);
    }
}
