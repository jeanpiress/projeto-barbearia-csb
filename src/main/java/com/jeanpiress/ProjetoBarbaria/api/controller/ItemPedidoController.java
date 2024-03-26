package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ItemPedidoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbaria.domain.services.ItemPedidoService;
import com.jeanpiress.ProjetoBarbaria.repositories.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/itemPedidos")
public class ItemPedidoController {

    @Autowired
    private ItemPedidoRepository repository;

    @Autowired
    private ItemPedidoService service;

    @GetMapping
    public ResponseEntity<List<ItemPedido>> listar(){
        List<ItemPedido> itemPedidoList = repository.findAll();
        return ResponseEntity.ok(itemPedidoList);
    }

    @GetMapping(value = "/{itemPedidoId}")
    public ResponseEntity<ItemPedido> buscarPorId(@PathVariable Long itemPedidoId) {
        ItemPedido itemPedido = service.buscarPorId(itemPedidoId);

        return ResponseEntity.ok(itemPedido);
    }

    @PostMapping
    public ResponseEntity<ItemPedido> adicionar(@RequestBody ItemPedido itemPedido) {
        ItemPedido itemPedidoCriado = service.adicionar(itemPedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemPedidoCriado);
    }

    @DeleteMapping("/{itemPedidoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long itemPedidoId){
        service.remover(itemPedidoId);

    }

}
