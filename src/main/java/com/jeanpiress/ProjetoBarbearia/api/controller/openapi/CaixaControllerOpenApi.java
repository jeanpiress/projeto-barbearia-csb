package com.jeanpiress.ProjetoBarbearia.api.controller.openapi;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.CaixaModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Caixa")
public interface CaixaControllerOpenApi {


    @Operation(summary ="Busca o caixa aberto")
    public ResponseEntity<CaixaModel> buscarCaixaDiario();

    @Operation(summary ="Fecha o caixa")
    @ApiResponse(responseCode = "204", description = "Caixa fechado")
    public void fecharCaixa();
}
