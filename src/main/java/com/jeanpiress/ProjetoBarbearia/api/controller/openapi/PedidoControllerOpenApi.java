package com.jeanpiress.ProjetoBarbearia.api.controller.openapi;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.PedidoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.PedidoAlteracaoInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.PedidoInput;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.FormaPagamentoJson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Pedidos")
public interface PedidoControllerOpenApi {

    @Operation(summary ="Lista os pedidos por status pagamento e com caixa aberto ou fechado")
    public ResponseEntity<List<PedidoDto>> listarPagosCaixaAberto(@Parameter(description = "Informa se o caixa esta aberto")
                                                                       Boolean caixaAberto,
                                                                     @Parameter(description = "Representação o status do pagamento")
                                                                       String statusPagamento);

    @Operation(summary ="Lista os pedidos por status pagamento e pedido")
    public ResponseEntity<List<PedidoDto>> listarPedidosFiltroStatus(@Parameter(description = "Representação o status do pedido")
                                                                        String statusPedido,
                                                                     @Parameter(description = "Representação o status do pagamento")
                                                                        String statusPagamento);

    @Operation(summary ="Busca Pedido por id", responses ={
            @ApiResponse(responseCode = "400", description = "Id do Pedido inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<PedidoDto> buscarPorId(@Parameter(description = "ID de um pedido", example = "1")
                                                         Long pedidoId);
    @Operation(summary ="Cria um novo Pedido", responses ={
            @ApiResponse(responseCode = "201", description = "Pedido cadastrado"),
            @ApiResponse(responseCode = "400", description = "Id do pedido inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<PedidoDto> adicionar(@Parameter(description = "Representação de um novo pedido")
                                                       PedidoInput pedidoInput,
                                               @Parameter(description = "status do pedido", example = "AGENDADO")
                                                       String statusPedido);
   
    @Operation(summary ="Altera um pedido", responses ={
            @ApiResponse(responseCode = "200", description = "Pedido atualizado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<PedidoDto> alterar(@Parameter(description = "Representação de um novo pedido com novos dados")
                                                 PedidoAlteracaoInput pedidoInput,
                                             @Parameter(description = "ID de um pedido", example = "1")
                                                     Long pedidoId);

    @Operation(summary ="Altera profissional de um pedido", responses ={
            @ApiResponse(responseCode = "200", description = "Pedido atualizado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<PedidoDto> alterarProfissional(@Parameter(description = "Representação de um profissional")
                                                             Long pedidoInput,
                                                         @Parameter(description = "ID de um pedido", example = "1")
                                                             Long pedidoId);

    @Operation(summary ="Cancelar um Pedido", responses ={
            @ApiResponse(responseCode = "204", description = "Pedido cancelado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public void cancelar(@Parameter(description = "ID de um pedido", example = "1")
                            Long pedidoId);

    @Operation(summary ="Exclui um Pedido", responses ={
            @ApiResponse(responseCode = "204", description = "Pedido excluido"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public void excluir(@Parameter(description = "ID de um pedido", example = "1")
                         Long pedidoId);


    @Operation(summary ="Adiciona um ItemPedido", responses ={
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<PedidoDto> adicionarItemPedido(@Parameter(description = "ID de um pedido", example = "1")
                                                             Long pedidoId,
                                                         @Parameter(description = "ID de um ItemPedido", example = "1")
                                                             Long itemPedidoId);

    @Operation(summary ="Remove um ItemPedido", responses ={
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<PedidoDto> removerItemPedido(@Parameter(description = "ID de um pedido", example = "1")
                                                           Long pedidoId,
                                                       @Parameter(description = "ID de um ItemPedido", example = "1")
                                                           Long itemPedidoId);

    @Operation(summary ="Efetua o pagamento de um pedido", responses ={
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<PedidoDto> efetuarPagamento(@Parameter(description = "Representação de uma nova forma de pagamento")
                                                          FormaPagamentoJson formaPagamento,
                                                      @Parameter(description = "ID de um pedido", example = "1")
                                                          Long pedidoId);


    @Operation(summary ="Confirma um pedido", responses ={
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public void confirmarPedido(@Parameter(description = "ID de um pedido", example = "1")
                                    @PathVariable Long pedidoId);
}


