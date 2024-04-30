package com.jeanpiress.ProjetoBarbaria.api.controller.openapi;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.ItemPedidoDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.ComissaoDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.ItemPedidoDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.ItemPedidoInput;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.ComissaoInput;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.ItemPedidoInput;
import com.jeanpiress.ProjetoBarbaria.api.exceptionHandlers.Problem;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ItemPedidoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.NegocioException;
import com.jeanpiress.ProjetoBarbaria.domain.model.ItemPedido;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "ItensPedido")
public interface ItemPedidoControllerOpenApi {

    @ApiOperation("Lista os ItensPedido")
    public ResponseEntity<List<ItemPedidoDto>> listar();

    @ApiOperation("Busca ItemPedido por id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Id do ItemPedido inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "ItemPedido não encontrado", response = Problem.class)
    })
    public ResponseEntity<ItemPedidoDto> buscarPorId(@ApiParam(value = "ID de um itemPedido", example = "1")
                                                         Long itemPedidoId);
    @ApiOperation("Cria um novo ItemPedido")
    @ApiResponses({
            @ApiResponse(code = 201, message = "ItemPedido cadastrado"),
            @ApiResponse(code = 400, message = "Id do itemPedido inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "ItemPedido não encontrado", response = Problem.class)
    })
    public ResponseEntity<ItemPedidoDto> adicionar(@ApiParam(name = "Corpo", value = "Representação de um novo itemPedido")
                                                       ItemPedidoInput itemPedidoInput);
   
    @ApiOperation("Altera um itemPedido")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ItemPedido atualizado"),
            @ApiResponse(code = 404, message = "ItemPedido não encontrado", response = Problem.class)
    })
    public ResponseEntity<ItemPedidoDto> alterar(@ApiParam(name = "Corpo", value = "Representação de um novo itemPedido com novos dados")
                                                     ItemPedidoInput itemPedidoInput,
                                                 @ApiParam(value = "ID de um itemPedido", example = "1")
                                                     Long itemPedidoId);
    @ApiOperation("Apaga um ItemPedido")
    @ApiResponses({
            @ApiResponse(code = 204, message = "ItemPedido excluido"),
            @ApiResponse(code = 404, message = "ItemPedido não encontrado", response = Problem.class)
    })
    public void deletar(@ApiParam(value = "ID de um itemPedido", example = "1")
                            Long itemPedidoId);


}


