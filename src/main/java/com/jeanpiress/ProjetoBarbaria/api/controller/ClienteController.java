package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ClienteNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.services.ClienteService;
import com.jeanpiress.ProjetoBarbaria.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController()
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService service;

    @GetMapping
    public ResponseEntity<List<Cliente>> listar(){
        List<Cliente> clienteList = clienteRepository.findAll();
        return ResponseEntity.ok(clienteList);
    }

    @GetMapping(value = "/{clienteId}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId).
                orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));

        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity<Cliente> adicionar(@RequestBody Cliente cliente) {
        Cliente clienteCriado = service.adicionar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCriado);
    }

    @DeleteMapping("/{clienteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long clienteId){
        service.remover(clienteId);

    }

}
