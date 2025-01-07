package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ItemPedidoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.ItemPedidoInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ItemPedidoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ItemPedidoInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProdutoId;
import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.model.PacotePronto;
import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ItemPedidoRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.ItemPedidoService;
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
class ItemPedidoControllerTest {

    @InjectMocks
    ItemPedidoController itemPedidoController;

    @Mock
    ItemPedidoService itemPedidoService;

    @Mock
    ItemPedidoRepository itemPedidoRepository;

    @Mock
    ItemPedidoAssembler itemPedidoAssembler;

    @Mock
    ItemPedidoInputDissembler itemPedidoInputDissembler;

    MockMvc mockMvc;
    ItemPedido itemPedido;
    ItemPedidoDto itemPedidoDto;
    ItemPedidoInput itemPedidoInput;
    Profissional profissional;
    ItemPacote itemPacote;
    PacotePronto pacotePronto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(itemPedidoController).alwaysDo(print()).build();

        profissional = new Profissional(1L, "João Silva", "João", "34999999999",
                null, LocalDate.parse("1991-11-13"), BigDecimal.ZERO, null, true, null);
        itemPedido = new ItemPedido(1L, null, null, 2, null, Produto.builder().id(1L).build());
        itemPacote = new ItemPacote(1L, itemPedido, profissional, null);
        pacotePronto = new PacotePronto(1L, "2 barbas", null, 30, true, BigDecimal.valueOf(50),
                BigDecimal.ZERO, BigDecimal.ZERO, List.of(itemPacote), Categoria.builder().build());
        itemPedidoDto = new ItemPedidoDto();
        itemPedidoInput = new ItemPedidoInput(2, ProdutoId.builder().id(1L).build());
    }

    @Test
    void deveListarTodosItensPedido() throws Exception {
        List<ItemPedido> itensPedido = Arrays.asList(itemPedido);

        when(itemPedidoRepository.findAll()).thenReturn(itensPedido);
        when(itemPedidoAssembler.collectionToModel(itensPedido)).thenReturn(Arrays.asList(itemPedidoDto));

        mockMvc.perform(get("/itemPedidos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(itemPedidoRepository).findAll();
        verifyNoMoreInteractions(itemPedidoRepository);
    }

    @Test
    void deveBuscarItemPedidoPorId() throws Exception {
        when(itemPedidoService.buscarPorId(anyLong())).thenReturn(itemPedido);
        when(itemPedidoAssembler.toModel(any(ItemPedido.class))).thenReturn(itemPedidoDto);

        mockMvc.perform(get("/itemPedidos/{itemPedidoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(itemPedidoService).buscarPorId(1L);
        verifyNoMoreInteractions(itemPedidoService);
    }

    @Test
    void deveAdicionarUmNovoItemPedido() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(itemPedidoService.adicionar(any(ItemPedido.class))).thenReturn(itemPedido);


        mockMvc.perform(post("/itemPedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemPedido)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(itemPedidoService).adicionar(any());
        verifyNoMoreInteractions(itemPedidoService);
    }

    @Test
    void deveAlterarUmItemPedido() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(itemPedidoService.buscarPorId(anyLong())).thenReturn(itemPedido);
        doNothing().when(itemPedidoInputDissembler).copyToDomainObject(any(ItemPedidoInput.class), any(ItemPedido.class));
        when(itemPedidoService.adicionar(any(ItemPedido.class))).thenReturn(itemPedido);
        when(itemPedidoAssembler.toModel(any(ItemPedido.class))).thenReturn(itemPedidoDto);

        mockMvc.perform(put("/itemPedidos/{itemPedidoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemPedido)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(itemPedidoService).buscarPorId(1L);
        verify(itemPedidoService).adicionar(any(ItemPedido.class));
        verifyNoMoreInteractions(itemPedidoService);
    }

    @Test
    void deveDeletarUmItemPedido() throws Exception {
        doNothing().when(itemPedidoService).remover(anyLong());

        mockMvc.perform(delete("/itemPedidos/{itemPedidoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(itemPedidoService).remover(1L);
        verifyNoMoreInteractions(itemPedidoService);
    }
}
