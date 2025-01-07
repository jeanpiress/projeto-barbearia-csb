package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.CaixaModel;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.FaturamentoDia;
import com.jeanpiress.ProjetoBarbearia.domain.services.CaixaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
class CaixaControllerTest {

    @InjectMocks
    CaixaController caixaController;

    @Mock
    CaixaService caixaService;

    MockMvc mockMvc;

    CaixaModel caixaModel;

    FaturamentoDia faturamentoDia;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(caixaController).alwaysDo(print()).build();

        faturamentoDia = new FaturamentoDia(LocalDate.now(), BigDecimal.valueOf(100), BigDecimal.valueOf(100), 1);

        caixaModel = new CaixaModel(BigDecimal.valueOf(100), BigDecimal.valueOf(200), BigDecimal.valueOf(300), BigDecimal.valueOf(400),
                BigDecimal.valueOf(500), BigDecimal.valueOf(600), BigDecimal.valueOf(1000), 6, 12, List.of(faturamentoDia));

    }

    @Test
    public void deveBuscarCaixaDiario() throws Exception {
        Mockito.when(caixaService.gerarCaixaDiario()).thenReturn(caixaModel);

        mockMvc.perform(get("/caixa")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(caixaService).gerarCaixaDiario();
        verifyNoMoreInteractions(caixaService);

    }

    @Test
    public void deveFecharCaixaDiario() throws Exception {
        mockMvc.perform(delete("/caixa/fechar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();

        verify(caixaService).fecharCaixa();
        verifyNoMoreInteractions(caixaService);
    }
}