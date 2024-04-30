package com.jeanpiress.ProjetoBarbaria.api.controller.openapi;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.ProfissionalDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.ProfissionalInput;
import com.jeanpiress.ProjetoBarbaria.api.exceptionHandlers.Problem;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Profissionais")
public interface ProfissionalControllerOpenApi {

    @ApiOperation("Lista os Profissionais")
    public ResponseEntity<List<ProfissionalDto>> listar();

    @ApiOperation("Busca Profissional por id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Id do Profissional inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Profissional não encontrado", response = Problem.class)
    })
    public ResponseEntity<ProfissionalDto> buscarPorId(@ApiParam(value = "ID de um profissional", example = "1")
                                                         Long profissionalId);
    @ApiOperation("Cria um novo Profissional")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Profissional cadastrado"),
            @ApiResponse(code = 400, message = "Id do profissional inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Profissional não encontrado", response = Problem.class)
    })
    public ResponseEntity<ProfissionalDto> adicionar(@ApiParam(name = "Corpo", value = "Representação de um novo profissional")
                                                       ProfissionalInput profissionalInput);
   
    @ApiOperation("Altera um profissional")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Profissional atualizado"),
            @ApiResponse(code = 404, message = "Profissional não encontrado", response = Problem.class)
    })
    public ResponseEntity<ProfissionalDto> alterar(@ApiParam(name = "Corpo", value = "Representação de um novo profissional com novos dados")
                                                     ProfissionalInput profissionalInput,
                                                 @ApiParam(value = "ID de um profissional", example = "1")
                                                     Long profissionalId);
    @ApiOperation("Apaga um Profissional")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Profissional excluido"),
            @ApiResponse(code = 404, message = "Profissional não encontrado", response = Problem.class)
    })
    public void deletar(@ApiParam(value = "ID de um profissional", example = "1")
                            Long profissionalId);


}


