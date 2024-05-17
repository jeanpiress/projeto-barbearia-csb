package com.jeanpiress.ProjetoBarbearia.api.controller.openapi;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.PacoteProntoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.PacoteProntoInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "PacotePronto")
public interface PacoteProntoControllerOpenApi {

    @Operation(summary ="Lista os pacotes prontons")
    public ResponseEntity<List<PacoteProntoDto>> listar();

    @Operation(summary ="Lista os pacotes prontos ativos")
    public ResponseEntity<List<PacoteProntoDto>> listarAtivos();

    @Operation(summary ="Busca pacote pronto por id", responses ={
            @ApiResponse(responseCode = "404", description = "Pacote pronto não encontrado", content = @Content(schema = @Schema))

    })
    public ResponseEntity<PacoteProntoDto> pacoteProntoPorId(@Parameter(description = "ID de um pacote pronto", example = "1")
                                                                 Long pacoteProntoId);

    @Operation(summary ="Cria um pacote pronto", responses ={
            @ApiResponse(responseCode = "201", description = "Pacote pronto cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema)),
            @ApiResponse(responseCode = "406", description = "Mensagem incompreensivel", content = @Content(schema = @Schema))
    })
    public ResponseEntity<PacoteProntoDto> criarPacotePronto(@Parameter(description = "Representação de um novo pacote pronto")
                                                                 PacoteProntoInput pacoteProntoInput);

}
