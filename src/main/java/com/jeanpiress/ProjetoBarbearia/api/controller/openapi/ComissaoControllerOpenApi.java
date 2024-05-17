package com.jeanpiress.ProjetoBarbearia.api.controller.openapi;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ComissaoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ComissaoInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Comissoes")
public interface ComissaoControllerOpenApi {

    @Operation(summary ="Lista as comissoes")
    public ResponseEntity<List<ComissaoDto>> listar();

    @Operation(summary ="Busca comissão por id", responses ={
            @ApiResponse(responseCode = "400", description = "Id da comissão inválida", content = @Content(schema = @Schema)),
            @ApiResponse(responseCode = "404", description = "comissão não encontrada", content = @Content(schema = @Schema))
    })
    public ResponseEntity<ComissaoDto> buscarPorId(@Parameter(description = "ID de uma comissão", example = "1")
                                                       Long comissaoId);
    @Operation(summary ="Cria uma nova comissão", responses ={
            @ApiResponse(responseCode = "201", description = "Comissão cadastrada"),
            @ApiResponse(responseCode = "400", description = "Id da comissão inválida", content = @Content(schema = @Schema)),
            @ApiResponse(responseCode = "404", description = "Comissão não encontrada", content = @Content(schema = @Schema))
    })
    public ResponseEntity<ComissaoDto> adicionar(@Parameter(description = "Representação de uma nova comissão")
                                                     ComissaoInput comissaoInput);
    @Operation(summary ="Altera uma categoria", responses ={
            @ApiResponse(responseCode = "200", description = "Comissão atualizada"),
            @ApiResponse(responseCode = "404", description = "Comissão não encontrada", content = @Content(schema = @Schema))
    })
    public ResponseEntity<ComissaoDto> alterar(@Parameter(description = "Representação de uma nova comissão com novos dados")
                                                   ComissaoInput comissaoInput,
                                               @Parameter(description = "ID de uma comissão", example = "1")
                                                   Long comissaoId);

    @Operation(summary ="Apaga uma categoria", responses ={
            @ApiResponse(responseCode = "204", description = "Comissão excluida"),
            @ApiResponse(responseCode = "404", description = "Comissão não encontrada", content = @Content(schema = @Schema))
    })
    public void deletar(@Parameter(description = "ID de uma comissão", example = "1")
                            Long comissaoId);
}
