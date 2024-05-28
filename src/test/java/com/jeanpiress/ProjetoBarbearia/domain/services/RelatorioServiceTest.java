package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.DataJsonInicioFim;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class RelatorioServiceTest {

    @InjectMocks
    RelatorioService relatorioService;

    DataJsonInicioFim dataJson;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
       // dataJson = new DataJsonInicioFim("2024");
    }

    public void deveBuscarPedidosPorDataJson(){

    }

}