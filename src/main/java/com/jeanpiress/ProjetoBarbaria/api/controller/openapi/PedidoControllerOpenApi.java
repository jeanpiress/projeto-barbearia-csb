package com.jeanpiress.ProjetoBarbaria.api.controller.openapi;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.PedidoDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.PedidoAlteracaoInput;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.PedidoInput;
import com.jeanpiress.ProjetoBarbaria.api.exceptionHandlers.Problem;
import com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao.FormaPagamentoJson;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api(tags = "Pedidos")
public interface PedidoControllerOpenApi {

    @ApiOperation("Lista os pedidos")
    public ResponseEntity<List<PedidoDto>> listar();

    @ApiOperation("Busca Pedido por id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Id do Pedido inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Pedido não encontrado", response = Problem.class)
    })
    public ResponseEntity<PedidoDto> buscarPorId(@ApiParam(value = "ID de um pedido", example = "1")
                                                         Long pedidoId);
    @ApiOperation("Cria um novo Pedido")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pedido cadastrado"),
            @ApiResponse(code = 400, message = "Id do pedido inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Pedido não encontrado", response = Problem.class)
    })
    public ResponseEntity<PedidoDto> adicionar(@ApiParam(name = "Corpo", value = "Representação de um novo pedido")
                                                       PedidoInput pedidoInput);
   
    @ApiOperation("Altera um pedido")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pedido atualizado"),
            @ApiResponse(code = 404, message = "Pedido não encontrado", response = Problem.class)
    })
    public ResponseEntity<PedidoDto> alterar(@ApiParam(name = "Corpo", value = "Representação de um novo pedido com novos dados")
                                                 PedidoAlteracaoInput pedidoInput,
                                             @ApiParam(value = "ID de um pedido", example = "1")
                                                     Long pedidoId);

    @ApiOperation("Cancelar um Pedido")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Pedido cancelado"),
            @ApiResponse(code = 404, message = "Pedido não encontrado", response = Problem.class)
    })
    public void cancelar(@ApiParam(value = "ID de um pedido", example = "1")
                            Long pedidoId);


    @ApiOperation("Adiciona um ItemPedido")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Pedido não encontrado", response = Problem.class)
    })
    public ResponseEntity<PedidoDto> adicionarItemPedido(@ApiParam(value = "ID de um pedido", example = "1")
                                                             Long pedidoId,
                                                         @ApiParam(value = "ID de um ItemPedido", example = "1")
                                                             Long itemPedidoId);

    @ApiOperation("Remove um ItemPedido")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Pedido não encontrado", response = Problem.class)
    })
    public ResponseEntity<PedidoDto> removerItemPedido(@ApiParam(value = "ID de um pedido", example = "1")
                                                           Long pedidoId,
                                                       @ApiParam(value = "ID de um ItemPedido", example = "1")
                                                           Long itemPedidoId);

    @ApiOperation("Efetua o pagamento de um pedido")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Pedido não encontrado", response = Problem.class)
    })
    public ResponseEntity<PedidoDto> efetuarPagamento(@ApiParam(name = "Corpo", value = "Representação de uma nova forma de pagamento")
                                                          FormaPagamentoJson formaPagamento,
                                                      @ApiParam(value = "ID de um pedido", example = "1")
                                                          Long pedidoId);


    @ApiOperation("Confirma um pedido")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Pedido não encontrado", response = Problem.class)
    })
    public void confirmarPedido(@ApiParam(value = "ID de um pedido", example = "1")
                                    @PathVariable Long pedidoId);
}


