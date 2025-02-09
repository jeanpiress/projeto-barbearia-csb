package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.ItemPedidoControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ItemPedidoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.ItemPedidoInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ItemPedidoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ItemPedidoInput;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ItemPedidoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.NegocioException;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbearia.domain.services.ItemPedidoService;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping(path ="/itemPedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemPedidoController implements ItemPedidoControllerOpenApi {

    @Autowired
    private ItemPedidoRepository repository;

    @Autowired
    private ItemPedidoService service;

    @Autowired
    private ItemPedidoAssembler itemPedidoAssembler;

    @Autowired
    private ItemPedidoInputDissembler itemPedidoDissembler;

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping
    public ResponseEntity<List<ItemPedidoDto>> listar(){
        List<ItemPedido> itensPedido = repository.findAll();
        List<ItemPedidoDto> itensPedidoDto = itemPedidoAssembler.collectionToModel(itensPedido);
        return ResponseEntity.ok(itensPedidoDto);
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping(value = "/{itemPedidoId}")
    public ResponseEntity<ItemPedidoDto> buscarPorId(@PathVariable Long itemPedidoId) {
        ItemPedido itemPedido = service.buscarPorId(itemPedidoId);
        ItemPedidoDto itemPedidoDto = itemPedidoAssembler.toModel(itemPedido);
        return ResponseEntity.ok(itemPedidoDto);
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @PostMapping
    public ResponseEntity<ItemPedidoDto> adicionar(@RequestBody @Valid ItemPedidoInput itemPedidoInput) {
        ItemPedido itemPedido = itemPedidoDissembler.toDomainObject(itemPedidoInput);
        ItemPedido itemPedidoCriado = service.adicionar(itemPedido);
        ItemPedidoDto itemPedidoDto = itemPedidoAssembler.toModel(itemPedidoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemPedidoDto);
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @PutMapping(value = "/{itemPedidoId}")
    public ResponseEntity<ItemPedidoDto> alterar(@RequestBody @Valid ItemPedidoInput itemPedidoInput, @PathVariable Long itemPedidoId) {
        ItemPedido itemPedido = service.buscarPorId(itemPedidoId);
        itemPedidoDissembler.copyToDomainObject(itemPedidoInput, itemPedido);
        ItemPedidoDto itemPedidoDto = itemPedidoAssembler.toModel(service.adicionar(itemPedido));
        return ResponseEntity.status(HttpStatus.CREATED).body(itemPedidoDto);
    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @DeleteMapping("/{itemPedidoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long itemPedidoId){
        service.remover(itemPedidoId);

    }

}
