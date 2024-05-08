package com.jeanpiress.ProjetoBarbaria.api.controller.openapi;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.ClienteDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.ClienteInput;
import com.jeanpiress.ProjetoBarbaria.api.exceptionHandlers.Problem;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Clientes")
public interface ClienteControllerOpenApi {

    @ApiOperation("Lista os clientes")
    public ResponseEntity<List<ClienteDto>> listar(@ApiParam("nome de um cliente")
                                                       String nome);


    @ApiOperation("Busca cliente por id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Id do cliente inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "cliente não encontrado", response = Problem.class)
    })
    public ResponseEntity<ClienteDto> buscarPorId(@ApiParam(value = "ID de um cliente", example = "1")
                                                      Long clienteId);


    @ApiOperation("Cria um novo cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente cadastrado")
    })
    public ResponseEntity<ClienteDto> adicionar(@ApiParam(name = "Corpo", value = "Representação de um novo cliente")
                                                    ClienteInput clienteInput);


    @ApiOperation("Altera um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente atualizado"),
            @ApiResponse(code = 404, message = "cliente não encontrado", response = Problem.class)
    })
    public ResponseEntity<ClienteDto> alterar(@ApiParam(name = "Corpo", value = "Representação de um novo cliente com novos dados")
                                                  ClienteInput clienteInput,
                                              @ApiParam(value = "ID de um cliente", example = "1")
                                                  Long clienteId);


    @ApiOperation("Apaga um cliente")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cliente excluido"),
            @ApiResponse(code = 404, message = "cliente não encontrado", response = Problem.class)
    })
    public void deletar(@ApiParam(value = "ID de um cliente", example = "1")
                            Long clienteId);


}
