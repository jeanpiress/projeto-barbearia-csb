package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.api.controller.converteDto.assebler.ClienteAssembler;
import com.jeanpiress.ProjetoBarbaria.api.controller.converteDto.assebler.dissembler.ClienteInputDissembler;
import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.ClienteDto;
import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.input.ClienteInput;
import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.services.ClienteService;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private ClienteService service;

    @Autowired
    private ClienteAssembler clienteAssembler;

    @Autowired
    private ClienteInputDissembler clienteInputDissembler;

    @GetMapping
    public ResponseEntity<List<ClienteDto>> listar(){
       List<Cliente> clienteList = repository.findAll();
       List<ClienteDto> clientesDto = clienteAssembler.collectionToModel(clienteList);
       return ResponseEntity.ok(clientesDto);
    }

    @GetMapping(value = "/{clienteId}")
    public ResponseEntity<ClienteDto> buscarPorId(@PathVariable Long clienteId) {
        Cliente cliente = service.buscarPorId(clienteId);
        ClienteDto clienteDto = clienteAssembler.toModel(cliente);
        return ResponseEntity.ok(clienteDto);
    }

    @PostMapping
    public ResponseEntity<ClienteDto> adicionar(@RequestBody ClienteInput clienteInput) {
        Cliente cliente = clienteInputDissembler.toDomainObject(clienteInput);
        Cliente clienteCriado = service.adicionar(cliente);
        ClienteDto clienteDto = clienteAssembler.toModel(clienteCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDto);
    }

    @DeleteMapping("/{clienteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long clienteId){
        service.remover(clienteId);

    }

}
