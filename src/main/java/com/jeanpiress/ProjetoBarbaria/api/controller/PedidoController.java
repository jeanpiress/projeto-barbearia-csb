package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.domain.model.Pedido;
import com.jeanpiress.ProjetoBarbaria.domain.services.PedidoService;
import com.jeanpiress.ProjetoBarbaria.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private PedidoService service;


    @GetMapping
    public ResponseEntity<List<Pedido>> listar(){
        List<Pedido> pedidoList = repository.findAll();
        return ResponseEntity.ok(pedidoList);
    }

    @GetMapping(value = "/{pedidoId}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long pedidoId) {
        Pedido pedido = service.buscarPorId(pedidoId);

        return ResponseEntity.ok(pedido);
    }

    @PostMapping
    public ResponseEntity<Pedido> adicionar(@RequestBody Pedido pedido) {
        Pedido pedidoCriado = service.adicionar(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoCriado);

    }

    @DeleteMapping("/{pedidoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long pedidoId){
        service.remover(pedidoId);

    }

}
