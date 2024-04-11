package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.api.converteDto.assebler.PedidoAssembler;
import com.jeanpiress.ProjetoBarbaria.api.converteDto.dissembler.PedidoInputDissembler;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.PedidoDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.PedidoInput;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.FormaPagamentoStr;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.PedidoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.NegocioException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Pedido;
import com.jeanpiress.ProjetoBarbaria.domain.services.PedidoService;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private PedidoService service;

    @Autowired
    private PedidoAssembler pedidoAssembler;

    @Autowired
    private PedidoInputDissembler pedidoDissembler;
    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoDto>> listar(){
        List<Pedido> pedidos = repository.findByPagoAndCaixaAberto();
        List<PedidoDto> pedidosDto = pedidoAssembler.collectionToModel(pedidos);
        return ResponseEntity.ok(pedidosDto);
    }

    @GetMapping(value = "/{pedidoId}")
    public ResponseEntity<PedidoDto> buscarPorId(@PathVariable Long pedidoId) {
        Pedido pedido = service.buscarPorId(pedidoId);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedido);

        return ResponseEntity.ok(pedidoDto);
    }

    @PostMapping
    public ResponseEntity<PedidoDto> adicionar(@RequestBody @Valid PedidoInput pedidoInput) {
        Pedido pedido = pedidoDissembler.toDomainObject(pedidoInput);
        Pedido pedidoCriado = service.criar(pedido);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedidoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDto);

    }

    @DeleteMapping("/{pedidoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long pedidoId){
        service.remover(pedidoId);

    }
    @PutMapping("/{pedidoId}/add-item/{itemPedidoId}")
    public ResponseEntity<PedidoDto> adicionarItemPedido(@PathVariable Long pedidoId, @PathVariable Long itemPedidoId){
        Pedido pedido = service.adicionarItemPedido(pedidoId, itemPedidoId);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedido);
        return ResponseEntity.ok(pedidoDto);
    }

    @PutMapping(value = "/{pedidoId}")
    public ResponseEntity<PedidoDto> alterar(@RequestBody @Valid PedidoInput pedidoInput, @PathVariable Long pedidoId) {
        try {
            Pedido pedido = service.buscarPorId(pedidoId);
            pedidoDissembler.copyToDomainObject(pedidoInput, pedido);
            PedidoDto pedidoDto = pedidoAssembler.toModel(repository.save(pedido));
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDto);
        }catch(PedidoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{pedidoId}/remove-item/{itemPedidoId}")
    public ResponseEntity<PedidoDto> removerItemPedido(@PathVariable Long pedidoId, @PathVariable Long itemPedidoId){
        Pedido pedido = service.removerItemPedido(pedidoId, itemPedidoId);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedido);
        return ResponseEntity.ok(pedidoDto);
    }

    @PutMapping(value = "/{pedidoId}/pagar")
    public ResponseEntity<PedidoDto> efetuarPagamento(@RequestBody @Valid FormaPagamentoStr formaPagamento, @PathVariable @Valid Long pedidoId) {
        try {
            Pedido pedido = service.buscarPorId(pedidoId);
            pedidoService.adicionarFormaPagamento(formaPagamento, pedido);
            pedido.setStatusPagamento(StatusPagamento.PAGO);
            Pedido pedidoSalvo = repository.save(pedido);
            PedidoDto pedidoDto = pedidoAssembler.toModel(pedidoSalvo);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDto);
        }catch(PedidoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

}
