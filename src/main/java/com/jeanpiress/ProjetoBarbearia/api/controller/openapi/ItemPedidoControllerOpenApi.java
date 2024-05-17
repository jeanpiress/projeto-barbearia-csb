package com.jeanpiress.ProjetoBarbearia.api.controller.openapi;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ItemPedidoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ItemPedidoInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "ItensPedido")
public interface ItemPedidoControllerOpenApi {

    @Operation(summary ="Lista os ItensPedido")
    public ResponseEntity<List<ItemPedidoDto>> listar();

    @Operation(summary ="Busca ItemPedido por id", responses ={
            @ApiResponse(responseCode = "400", description = "Id do ItemPedido inválido", content = @Content(schema = @Schema)),
            @ApiResponse(responseCode = "404", description = "ItemPedido não encontrado", content = @Content(schema = @Schema))
    })
    public ResponseEntity<ItemPedidoDto> buscarPorId(@Parameter(description = "ID de um itemPedido", example = "1")
                                                         Long itemPedidoId);
    @Operation(summary ="Cria um novo ItemPedido", responses ={
            @ApiResponse(responseCode = "201", description = "ItemPedido cadastrado"),
            @ApiResponse(responseCode = "400", description = "Id do itemPedido inválido", content = @Content(schema = @Schema)),
            @ApiResponse(responseCode = "404", description = "ItemPedido não encontrado", content = @Content(schema = @Schema))
    })
    public ResponseEntity<ItemPedidoDto> adicionar(@Parameter(description = "Representação de um novo itemPedido")
                                                       ItemPedidoInput itemPedidoInput);
   
    @Operation(summary ="Altera um itemPedido", responses ={
            @ApiResponse(responseCode = "200", description = "ItemPedido atualizado"),
            @ApiResponse(responseCode = "404", description = "ItemPedido não encontrado", content = @Content(schema = @Schema))
    })
    public ResponseEntity<ItemPedidoDto> alterar(@Parameter(description = "Representação de um novo itemPedido com novos dados")
                                                     ItemPedidoInput itemPedidoInput,
                                                 @Parameter(description = "ID de um itemPedido", example = "1")
                                                     Long itemPedidoId);
    @Operation(summary ="Apaga um ItemPedido", responses ={
            @ApiResponse(responseCode = "204", description = "ItemPedido excluido"),
            @ApiResponse(responseCode = "404", description = "ItemPedido não encontrado", content = @Content(schema = @Schema))
    })
    public void deletar(@Parameter(description = "ID de um itemPedido", example = "1")
                            Long itemPedidoId);


}


