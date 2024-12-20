package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.PedidoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.PedidoInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.PedidoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.PedidoAlteracaoInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.PedidoInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.*;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPedido;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.FormaPagamentoJson;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.RealizacaoItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PedidoRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.PedidoService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class PedidoControllerTest {

    @InjectMocks
    PedidoController pedidoController;

    @Mock
    PedidoService pedidoService;

    @Mock
    PedidoRepository pedidoRepository;

    @Mock
    PedidoAssembler pedidoAssembler;

    @Mock
    PedidoInputDissembler pedidoDissembler;

    MockMvc mockMvc;
    Pedido pedido;
    PedidoDto pedidoDto;
    PedidoInput pedidoInput;
    PedidoAlteracaoInput pedidoAlteracaoInput;
    RealizacaoItemPacote realizacaoItemPacote;
    FormaPagamentoJson formaPagamento;
    Cliente cliente;
    Profissional profissional;
    Produto produto;
    ItemPedido itemPedido;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pedidoController).alwaysDo(print()).build();

        cliente = new Cliente(1L, "João", "34999999999", OffsetDateTime.parse("1991-11-13T00:00:00-03:00"),
                null, BigDecimal.ZERO, null, null, 30, profissional, null);
        profissional = new Profissional(1L, "João Silva", "João", "34999999999",
                null, OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), BigDecimal.ZERO, null, true, null);
        produto = new Produto(1L, "corte", BigDecimal.valueOf(45), true, false, null,
                false, BigDecimal.ONE, BigDecimal.ONE,null, BigDecimal.valueOf(50),
                Categoria.builder().id(1L).build(), null);
        itemPedido = new ItemPedido(1L, BigDecimal.valueOf(45), BigDecimal.valueOf(90), 2, null, produto);
        pedido = new Pedido(1L, OffsetDateTime.parse("2024-05-19T15:30:00-03:00"), List.of(itemPedido), StatusPagamento.PAGO,
                FormaPagamento.DINHEIRO, StatusPedido.FINALIZADO, cliente, profissional, BigDecimal.valueOf(45), BigDecimal.valueOf(90),
                BigDecimal.valueOf(90), true, BigDecimal.valueOf(90), OffsetDateTime.now(), null, null,
                null, null, null, null, null, Usuario.builder().build(), OffsetDateTime.now(), null);
        pedidoDto = new PedidoDto();
        pedidoInput = new PedidoInput(OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), ClienteId.builder().id(1L).build(),
                ProfissionalId.builder().id(1L).build());
        pedidoAlteracaoInput = PedidoAlteracaoInput.builder().horario(OffsetDateTime.now().plusDays(1)).
                profissional(ProfissionalId.builder().id(1L).build()).build();
        realizacaoItemPacote = new RealizacaoItemPacote(ProfissionalId.builder().id(1L).build(), PacoteId.builder().id(1L).build(),
                ItemPacoteId.builder().id(1L).build());
        formaPagamento = new FormaPagamentoJson("dinheiro");
    }

    @Test
    void deveListarTodosPedidos() throws Exception {
        List<Pedido> pedidos = Arrays.asList(pedido);

        when(pedidoRepository.findByPagoAndCaixaAberto()).thenReturn(pedidos);
        when(pedidoAssembler.collectionToModel(pedidos)).thenReturn(Arrays.asList(pedidoDto));

        mockMvc.perform(get("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(pedidoRepository).findByPagoAndCaixaAberto();
        verifyNoMoreInteractions(pedidoRepository);
    }

    @Test
    void deveBuscarPedidoPorId() throws Exception {
        when(pedidoService.buscarPorId(anyLong())).thenReturn(pedido);
        when(pedidoAssembler.toModel(any(Pedido.class))).thenReturn(pedidoDto);

        mockMvc.perform(get("/pedidos/{pedidoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(pedidoService).buscarPorId(1L);
        verifyNoMoreInteractions(pedidoService);
    }

    @Test
    void deveAdicionarUmNovoPedido() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(pedidoDissembler.toDomainObject(any(PedidoInput.class))).thenReturn(pedido);
        when(pedidoService.criar(any(Pedido.class))).thenReturn(pedido);
        when(pedidoAssembler.toModel(any(Pedido.class))).thenReturn(pedidoDto);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(pedidoDissembler).toDomainObject(any(PedidoInput.class));
        verify(pedidoService).criar(any(Pedido.class));
        verify(pedidoAssembler).toModel(any(Pedido.class));
        verifyNoMoreInteractions(pedidoService);
    }

    @Test
    void deveCancelarUmPedido() throws Exception {
        doNothing().when(pedidoService).cancelarPedido(anyLong());

        mockMvc.perform(delete("/pedidos/{pedidoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(pedidoService).cancelarPedido(1L);
        verifyNoMoreInteractions(pedidoService);
    }

    @Test
    void deveAdicionarItemPedido() throws Exception {
        when(pedidoService.adicionarItemPedido(anyLong(), anyLong())).thenReturn(pedido);
        when(pedidoAssembler.toModel(any(Pedido.class))).thenReturn(pedidoDto);

        mockMvc.perform(put("/pedidos/{pedidoId}/add-item/{itemPedidoId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(pedidoService).adicionarItemPedido(1L, 1L);
        verify(pedidoAssembler).toModel(any(Pedido.class));
        verifyNoMoreInteractions(pedidoService);
    }

    @Test
    void deveAlterarPedido() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(pedidoService.alterarInfoPedido(any(PedidoAlteracaoInput.class), anyLong())).thenReturn(pedido);
        when(pedidoAssembler.toModel(any())).thenReturn(pedidoDto);

        mockMvc.perform(put("/pedidos/{pedidoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoAlteracaoInput)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(pedidoService).alterarInfoPedido(any(PedidoAlteracaoInput.class), anyLong());
        verify(pedidoAssembler).toModel(any());
        verifyNoMoreInteractions(pedidoService);
    }

    @Test
    void deveRemoverItemPedido() throws Exception {
        when(pedidoService.removerItemPedido(anyLong(), anyLong())).thenReturn(pedido);
        when(pedidoAssembler.toModel(any(Pedido.class))).thenReturn(pedidoDto);

        mockMvc.perform(delete("/pedidos/{pedidoId}/remove-item/{itemPedidoId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(pedidoService).removerItemPedido(1L, 1L);
        verify(pedidoAssembler).toModel(any(Pedido.class));
        verifyNoMoreInteractions(pedidoService);
    }

    @Test
    void deveEfetuarPagamento() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(pedidoService.realizarPagamento(any(FormaPagamentoJson.class), anyLong())).thenReturn(pedido);
        when(pedidoAssembler.toModel(any(Pedido.class))).thenReturn(pedidoDto);

        mockMvc.perform(put("/pedidos/{pedidoId}/pagar", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formaPagamento)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(pedidoService).realizarPagamento(any(FormaPagamentoJson.class), anyLong());
        verify(pedidoAssembler).toModel(any(Pedido.class));
        verifyNoMoreInteractions(pedidoService);
    }

    @Test
    void deveEfetuarPagamentoComPacote() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(pedidoService.realizarPagamentoComPedidoExistente(any(RealizacaoItemPacote.class), anyLong())).thenReturn(pedido);
        when(pedidoAssembler.toModel(any(Pedido.class))).thenReturn(pedidoDto);

        mockMvc.perform(put("/pedidos/{pedidoId}/pagar/pacote", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(realizacaoItemPacote)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(pedidoService).realizarPagamentoComPedidoExistente(any(RealizacaoItemPacote.class), anyLong());
        verify(pedidoAssembler).toModel(any(Pedido.class));
        verifyNoMoreInteractions(pedidoService);
    }

    @Test
    void deveConfirmarPedido() throws Exception {
        doNothing().when(pedidoService).confirmarPedido(anyLong());

        mockMvc.perform(put("/pedidos/{pedidoId}/confirmar", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(pedidoService).confirmarPedido(1L);
        verifyNoMoreInteractions(pedidoService);
    }
}
