package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.api.converteDto.assebler.ItemPedidoAssembler;
import com.jeanpiress.ProjetoBarbaria.api.converteDto.dissembler.ItemPedidoInputDissembler;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.ItemPedidoDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.ItemPedidoInput;
import com.jeanpiress.ProjetoBarbaria.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbaria.domain.services.ItemPedidoService;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping("/itemPedidos")
public class ItemPedidoController {

    @Autowired
    private ItemPedidoRepository repository;

    @Autowired
    private ItemPedidoService service;

    @Autowired
    private ItemPedidoAssembler itemPedidoAssembler;

    @Autowired
    private ItemPedidoInputDissembler itemPedidoDissembler;

    @GetMapping
    public ResponseEntity<List<ItemPedidoDto>> listar(){
        List<ItemPedido> itensPedido = repository.findAll();
        List<ItemPedidoDto> itensPedidoDto = itemPedidoAssembler.collectionToModel(itensPedido);
        return ResponseEntity.ok(itensPedidoDto);
    }

    @GetMapping(value = "/{itemPedidoId}")
    public ResponseEntity<ItemPedidoDto> buscarPorId(@PathVariable Long itemPedidoId) {
        ItemPedido itemPedido = service.buscarPorId(itemPedidoId);
        ItemPedidoDto itemPedidoDto = itemPedidoAssembler.toModel(itemPedido);
        return ResponseEntity.ok(itemPedidoDto);
    }

    @PostMapping
    public ResponseEntity<ItemPedidoDto> adicionar(@RequestBody @Valid ItemPedidoInput itemPedidoInput) {
        ItemPedido itemPedido = itemPedidoDissembler.toDomainObject(itemPedidoInput);
        ItemPedido itemPedidoCriado = service.adicionar(itemPedido);
        ItemPedidoDto itemPedidoDto = itemPedidoAssembler.toModel(itemPedidoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemPedidoDto);
    }

    @DeleteMapping("/{itemPedidoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long itemPedidoId){
        service.remover(itemPedidoId);

    }

}
