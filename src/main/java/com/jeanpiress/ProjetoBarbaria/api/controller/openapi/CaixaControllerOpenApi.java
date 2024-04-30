package com.jeanpiress.ProjetoBarbaria.api.controller.openapi;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.CaixaModel;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

@Api(tags = "Caixa")
public interface CaixaControllerOpenApi {


    @ApiOperation("Busca o caixa aberto")
    public ResponseEntity<CaixaModel> buscarCaixaDiario();

    @ApiOperation("Fecha o caixa")
    @ApiResponse(code = 204, message = "Caixa fechado")
    public void fecharCaixa();
}
