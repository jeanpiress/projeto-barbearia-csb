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

import java.util.List;

@Tag(name = "Produtos")
public interface ProdutoControllerOpenApi {

    @Operation(summary ="Lista os Produtos")
    public ResponseEntity<List<ProdutoDto>> listar(@Parameter(description = "nome do produto") String nome,
                                                   @Parameter(description = "indica se é um produto ativo") boolean isAtivo,
                                                   @Parameter(description = "passa a categoria do produto a ser procurado") Long categoriaId);

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

    @Operation(summary ="Desativa um Produto", responses ={
            @ApiResponse(responseCode = "204", description = "Produto desativado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public void desativar(@Parameter(description = "ID de um produto", example = "1")
                        Long produtoId);

}


