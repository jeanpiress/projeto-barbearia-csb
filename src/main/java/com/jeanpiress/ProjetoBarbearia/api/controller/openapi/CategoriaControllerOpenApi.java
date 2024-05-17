package com.jeanpiress.ProjetoBarbearia.api.controller.openapi;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.CategoriaDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.CategoriaInput;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Categorias")
public interface CategoriaControllerOpenApi {


    @Operation(summary ="Lista as categorias")
    public ResponseEntity<List<CategoriaDto>> listar();

    @Operation(summary ="Busca categoria por id", responses = {
            @ApiResponse(responseCode = "400", description = "Id da categoria inválida", content = @Content(schema = @Schema)),
            @ApiResponse(responseCode = "404", description = "categoria não encontrada", content = @Content(schema = @Schema))
    })
    public ResponseEntity<CategoriaDto> buscarPorId(@Parameter(description = "ID de uma categoria", example = "1")
                                                        Long categoriaId);

    @Operation(summary ="Cria uma nova categoria", responses = {
            @ApiResponse(responseCode = "201", description = "Categoria cadastrada"),
            @ApiResponse(responseCode = "400", description = "Id da categoria inválida", content = @Content(schema = @Schema)),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content(schema = @Schema))
    })
    public ResponseEntity<CategoriaDto> adicionar(@Parameter(description = "Representação de uma nova categoria")
                                                      CategoriaInput categoriaInput);

    @Operation(summary ="Altera uma categoria", responses = {
            @ApiResponse(responseCode = "200", description = "categoria atualizada"),
            @ApiResponse(responseCode = "404", description = "categoria não encontrada", content = @Content(schema = @Schema))
    })
    public ResponseEntity<CategoriaDto> alterar(@Parameter(description = "Representação de uma nova categoria com novos dados")
                                                    CategoriaInput categoriaInput,
                                                @Parameter(description = "ID de um cliente", example = "1")
                                                     Long categoriaId);


    @Operation(summary ="Apaga uma categoria", responses = {
            @ApiResponse(responseCode = "204", description = "Categoria excluida"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content(schema = @Schema))
    })
    public void deletar(@Parameter(description = "ID de uma categoria", example = "1")
                        Long categoriaId);

}
