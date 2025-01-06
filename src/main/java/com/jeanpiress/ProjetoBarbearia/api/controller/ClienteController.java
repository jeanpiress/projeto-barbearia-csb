package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.ClienteControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ClienteAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.ClienteInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ClienteDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ClienteInput;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.CampoObrigatorioException;
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
import java.util.Objects;


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

    @PreAuthorize("hasAuthority('PROFISSIONAL')")
    @GetMapping
    public ResponseEntity<List<ClienteDto>> listar(@RequestParam String nome, @RequestParam boolean ativo){
        if(Objects.nonNull(nome)  && !nome.isBlank()){
            List<Cliente> clienteList = repository.findByNome(nome, ativo);
            List<ClienteDto> clientesDto = clienteAssembler.collectionToModel(clienteList);
            return ResponseEntity.ok(clientesDto);
        } else {
            throw new CampoObrigatorioException("Nome é obrigatorio");
        }
    }

    @PreAuthorize("hasAuthority('PROFISSIONAL')")
    @GetMapping(value = "/{clienteId}")
    public ResponseEntity<ClienteDto> buscarPorId(@PathVariable Long clienteId) {
        Cliente cliente = service.buscarPorId(clienteId);
        ClienteDto clienteDto = clienteAssembler.toModel(cliente);
        return ResponseEntity.ok(clienteDto);
    }

    @PreAuthorize("hasAuthority('PROFISSIONAL')")
    @PostMapping
    public ResponseEntity<ClienteDto> adicionar(@RequestBody @Valid ClienteInput clienteInput) {
        Cliente cliente = clienteDissembler.toDomainObject(clienteInput);
        Cliente clienteCriado = service.adicionar(cliente);
        ClienteDto clienteDto = clienteAssembler.toModel(clienteCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDto);
    }


    @PreAuthorize("hasAuthority('PROFISSIONAL')")
    @PutMapping(value = "/{clienteId}")
    public ResponseEntity<ClienteDto> alterar(@RequestBody @Valid ClienteInput clienteInput, @PathVariable Long clienteId) {
        Cliente cliente = service.buscarPorId(clienteId);
        clienteDissembler.copyToDomainObject(clienteInput, cliente);
        ClienteDto clienteDto = clienteAssembler.toModel(service.adicionar(cliente));
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDto);
    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @DeleteMapping("/{clienteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long clienteId){
        service.remover(clienteId);

    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @DeleteMapping("/{clienteId}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long clienteId){
        service.desativar(clienteId);

    }

}
