package com.jeanpiress.ProjetoBarbaria.api.controller.openapi;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.ProdutoDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.ProdutoInput;
import com.jeanpiress.ProjetoBarbaria.api.exceptionHandlers.Problem;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Produtos")
public interface ProdutoControllerOpenApi {

    @ApiOperation("Lista os Produtos")
    public ResponseEntity<List<ProdutoDto>> listar();

    @ApiOperation("Busca Produto por id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Id do Produto inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Produto não encontrado", response = Problem.class)
    })
    public ResponseEntity<ProdutoDto> buscarPorId(@ApiParam(value = "ID de um produto", example = "1")
                                                         Long produtoId);
    @ApiOperation("Cria um novo Produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Produto cadastrado"),
            @ApiResponse(code = 400, message = "Id do produto inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Produto não encontrado", response = Problem.class)
    })
    public ResponseEntity<ProdutoDto> adicionar(@ApiParam(name = "Corpo", value = "Representação de um novo produto")
                                                       ProdutoInput produtoInput);
   
    @ApiOperation("Altera um produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto atualizado"),
            @ApiResponse(code = 404, message = "Produto não encontrado", response = Problem.class)
    })
    public ResponseEntity<ProdutoDto> alterar(@ApiParam(name = "Corpo", value = "Representação de um novo produto com novos dados")
                                                     ProdutoInput produtoInput,
                                                 @ApiParam(value = "ID de um produto", example = "1")
                                                     Long produtoId);
    @ApiOperation("Apaga um Produto")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Produto excluido"),
            @ApiResponse(code = 404, message = "Produto não encontrado", response = Problem.class)
    })
    public void deletar(@ApiParam(value = "ID de um produto", example = "1")
                            Long produtoId);


}


