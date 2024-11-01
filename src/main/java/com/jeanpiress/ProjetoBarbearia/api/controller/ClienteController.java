package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.ClienteControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ClienteAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.ClienteInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ClienteDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ClienteInput;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ClienteNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.NegocioException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbearia.domain.services.ClienteService;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import java.util.List;


@RestController()
@RequestMapping(path = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClienteController implements ClienteControllerOpenApi {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private ClienteService service;

    @Autowired
    private ClienteAssembler clienteAssembler;

    @Autowired
    private ClienteInputDissembler clienteDissembler;

    @PreAuthorize("hasAuthority('GERENTE')")
    @GetMapping
    public ResponseEntity<List<ClienteDto>> listar(@RequestParam(required = false) String nome){
        List<Cliente> clienteList = (nome != null && !nome.isEmpty())
                ? repository.findByNome(nome)
                : repository.findAll();

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
    public ResponseEntity<ClienteDto> adicionar(@RequestBody @Valid ClienteInput clienteInput) {
        Cliente cliente = clienteDissembler.toDomainObject(clienteInput);
        Cliente clienteCriado = service.adicionar(cliente);
        ClienteDto clienteDto = clienteAssembler.toModel(clienteCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDto);
    }


    @PutMapping(value = "/{clienteId}")
    public ResponseEntity<ClienteDto> alterar(@RequestBody @Valid ClienteInput clienteInput, @PathVariable Long clienteId) {
        Cliente cliente = service.buscarPorId(clienteId);
        clienteDissembler.copyToDomainObject(clienteInput, cliente);
        ClienteDto clienteDto = clienteAssembler.toModel(service.adicionar(cliente));
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDto);
    }


    @DeleteMapping("/{clienteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long clienteId){
        service.remover(clienteId);

    }

}
