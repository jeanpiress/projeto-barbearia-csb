package com.jeanpiress.ProjetoBarbaria.api.controller.openapi;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.ComissaoDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.ComissaoInput;
import com.jeanpiress.ProjetoBarbaria.api.exceptionHandlers.Problem;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Comissoes")
public interface ComissaoControllerOpenApi {

    @ApiOperation("Lista as comissoes")
    public ResponseEntity<List<ComissaoDto>> listar();

    @ApiOperation("Busca comissão por id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Id da comissão inválida", response = Problem.class),
            @ApiResponse(code = 404, message = "comissão não encontrada", response = Problem.class)
    })
    public ResponseEntity<ComissaoDto> buscarPorId(@ApiParam(value = "ID de uma comissão", example = "1")
                                                       Long comissaoId);
    @ApiOperation("Cria uma nova comissão")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Comissão cadastrada"),
            @ApiResponse(code = 400, message = "Id da comissão inválida", response = Problem.class),
            @ApiResponse(code = 404, message = "Comissão não encontrada", response = Problem.class)
    })
    public ResponseEntity<ComissaoDto> adicionar(@ApiParam(name = "Corpo", value = "Representação de uma nova comissão")
                                                     ComissaoInput comissaoInput);
    @ApiOperation("Altera uma categoria")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Comissão atualizada"),
            @ApiResponse(code = 404, message = "Comissão não encontrada", response = Problem.class)
    })
    public ResponseEntity<ComissaoDto> alterar(@ApiParam(name = "Corpo", value = "Representação de uma nova comissão com novos dados")
                                                   ComissaoInput comissaoInput,
                                               @ApiParam(value = "ID de uma comissão", example = "1")
                                                   Long comissaoId);

    @ApiOperation("Apaga uma categoria")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Comissão excluida"),
            @ApiResponse(code = 404, message = "Comissão não encontrada", response = Problem.class)
    })
    public void deletar(@ApiParam(value = "ID de uma comissão", example = "1")
                            Long comissaoId);
}
