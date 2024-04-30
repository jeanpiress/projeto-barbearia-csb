package com.jeanpiress.ProjetoBarbaria.api.controller.openapi;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.CategoriaDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.CategoriaInput;
import com.jeanpiress.ProjetoBarbaria.api.exceptionHandlers.Problem;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Categorias")
public interface CategoriaControllerOpenApi {


    @ApiOperation("Lista as categorias")
    public ResponseEntity<List<CategoriaDto>> listar();

    @ApiOperation("Busca categoria por id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Id da categoria inválida", response = Problem.class),
            @ApiResponse(code = 404, message = "categoria não encontrada", response = Problem.class)
    })
    public ResponseEntity<CategoriaDto> buscarPorId(@ApiParam(value = "ID de uma categoria", example = "1")
                                                        Long categoriaId);

    @ApiOperation("Cria uma nova categoria")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Categoria cadastrada"),
            @ApiResponse(code = 400, message = "Id da categoria inválida", response = Problem.class),
            @ApiResponse(code = 404, message = "Categoria não encontrada", response = Problem.class)
    })
    public ResponseEntity<CategoriaDto> adicionar(@ApiParam(name = "Corpo", value = "Representação de uma nova categoria")
                                                      CategoriaInput categoriaInput);

    @ApiOperation("Altera uma categoria")
    @ApiResponses({
            @ApiResponse(code = 200, message = "categoria atualizada"),
            @ApiResponse(code = 404, message = "categoria não encontrada", response = Problem.class)
    })
    public ResponseEntity<CategoriaDto> alterar(@ApiParam(name = "Corpo", value = "Representação de uma nova categoria com novos dados")
                                                    CategoriaInput categoriaInput,
                                                @ApiParam(value = "ID de um cliente", example = "1")
                                                     Long categoriaId);


    @ApiOperation("Apaga uma categoria")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Categoria excluida"),
            @ApiResponse(code = 404, message = "Categoria não encontrada", response = Problem.class)
    })
    public void deletar(@ApiParam(value = "ID de uma categoria", example = "1")
                        Long categoriaId);

}
