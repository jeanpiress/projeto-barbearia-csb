package com.jeanpiress.ProjetoBarbearia.api.controller.openapi;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ProfissionalDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ProfissionalInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ProfissionalUsuarioInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Profissionais")
public interface ProfissionalControllerOpenApi {

    @Operation(summary ="Lista os Profissionais")
    public ResponseEntity<List<ProfissionalDto>> listar();

    @Operation(summary ="Lista os Profissionais com status ativo")
    public ResponseEntity<List<ProfissionalDto>> listarProfissionaisAtivos();

    @Operation(summary ="Lista os Profissionais com status inativo")
    public ResponseEntity<List<ProfissionalDto>> listarProfissionaisInativos();

    @Operation(summary ="Busca Profissional por id", responses ={
            @ApiResponse(responseCode = "400", description = "Id do Profissional inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Profissional não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<ProfissionalDto> buscarPorId(@Parameter(description = "ID de um profissional", example = "1")
                                                         Long profissionalId);
    @Operation(summary ="Cria um novo Profissional", responses ={
            @ApiResponse(responseCode = "201", description = "Profissional cadastrado"),
            @ApiResponse(responseCode = "400", description = "Id do profissional inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Profissional não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<ProfissionalDto> adicionar(@Parameter(description = "Representação de um novo profissional e um novo Usuario")
                                                     ProfissionalUsuarioInput input);
   
    @Operation(summary ="Altera um profissional", responses ={
            @ApiResponse(responseCode = "200", description = "Profissional atualizado"),
            @ApiResponse(responseCode = "404", description = "Profissional não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<ProfissionalDto> alterar(@Parameter(description = "Representação de um novo profissional com novos dados")
                                                     ProfissionalInput profissionalInput,
                                                 @Parameter(description = "ID de um profissional", example = "1")
                                                     Long profissionalId);
    @Operation(summary ="Apaga um Profissional", responses ={
            @ApiResponse(responseCode = "204", description = "Profissional excluido"),
            @ApiResponse(responseCode = "404", description = "Profissional não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public void deletar(@Parameter(description = "ID de um profissional", example = "1")
                            Long profissionalId);


}


