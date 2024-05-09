package com.jeanpiress.ProjetoBarbaria.api.controller.openapi;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.PacoteProntoDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.PacoteProntoInput;
import com.jeanpiress.ProjetoBarbaria.api.exceptionHandlers.Problem;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Api(tags = "PacotePronto")
public interface PacoteProntoControllerOpenApi {

    @ApiOperation("Lista os pacotes prontons")
    public ResponseEntity<List<PacoteProntoDto>> listar();

    @ApiOperation("Lista os pacotes prontos ativos")
    public ResponseEntity<List<PacoteProntoDto>> listarAtivos();

    @ApiOperation("Busca pacote pronto por id")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Pacote pronto não encontrado", response = Problem.class)

    })
    public ResponseEntity<PacoteProntoDto> pacoteProntoPorId(@ApiParam(value = "ID de um pacote pronto", example = "1")
                                                                 Long pacoteProntoId);

    @ApiOperation("Cria um pacote pronto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pacote pronto cadastrado"),
            @ApiResponse(code = 400, message = "Dados invalidos"),
            @ApiResponse(code = 400, message = "Mensagem incompreensivel")
    })
    public ResponseEntity<PacoteProntoDto> criarPacotePronto(@ApiParam(name = "Corpo", value = "Representação de um novo pacote pronto")
                                                                 PacoteProntoInput pacoteProntoInput);

}
