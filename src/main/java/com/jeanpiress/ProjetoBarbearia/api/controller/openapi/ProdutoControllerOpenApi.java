package com.jeanpiress.ProjetoBarbearia.api.controller.openapi;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ProdutoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ProdutoInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Produtos")
public interface ProdutoControllerOpenApi {

    @Operation(summary ="Lista os Produtos")
    public ResponseEntity<List<ProdutoDto>> listar();

    @Operation(summary ="Busca Produto por id", responses ={
            @ApiResponse(responseCode = "400", description = "Id do Produto inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<ProdutoDto> buscarPorId(@Parameter(description = "ID de um produto", example = "1")
                                                         Long produtoId);

    @Operation(summary ="lista os Produto por categoria", responses ={
            @ApiResponse(responseCode = "400", description = "Id da Categoria inválido", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<List<ProdutoDto>> buscarPorCategoria(@Parameter(description = "ID de uma categoria", example = "1")
                                                                   Long categoriaId);


    @Operation(summary ="Cria um novo Produto", responses ={
            @ApiResponse(responseCode = "201", description = "Produto cadastrado"),
            @ApiResponse(responseCode = "400", description = "Id do produto inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<ProdutoDto> adicionar(@Parameter(description = "Representação de um novo produto")
                                                       ProdutoInput produtoInput);
   
    @Operation(summary ="Altera um produto", responses ={
            @ApiResponse(responseCode = "201", description = "Produto atualizado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<ProdutoDto> alterar(@Parameter(description = "Representação de um novo produto com novos dados")
                                                     ProdutoInput produtoInput,
                                                 @Parameter(description = "ID de um produto", example = "1")
                                                     Long produtoId);
    @Operation(summary ="Apaga um Produto", responses ={
            @ApiResponse(responseCode = "204", description = "Produto excluido"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public void deletar(@Parameter(description = "ID de um produto", example = "1")
                            Long produtoId);


}


