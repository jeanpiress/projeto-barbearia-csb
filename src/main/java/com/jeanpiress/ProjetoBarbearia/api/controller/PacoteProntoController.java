package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.PacoteProntoControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.PacoteProntoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.PacoteProntoInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.PacoteProntoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.PacoteProntoInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.PacotePronto;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PacoteProntoRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.PacoteProntoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController()
@RequestMapping(path = "/pacotes-prontos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PacoteProntoController implements PacoteProntoControllerOpenApi {

    @Autowired
    private PacoteProntoRepository pacoteRepository;

    @Autowired
    private PacoteProntoAssembler pacoteProntoAssembler;

    @Autowired
    private PacoteProntoInputDissembler pacoteProntoDissembler;

    @Autowired
    private PacoteProntoService pacoteProntoService;


    @GetMapping
    public ResponseEntity<List<PacoteProntoDto>> listar(){
        return ResponseEntity.ok(pacoteProntoAssembler.collectionToModel(pacoteRepository.findAll()));
    }

    @GetMapping(value = "/ativo")
    public ResponseEntity<List<PacoteProntoDto>> listarAtivos(){
        return ResponseEntity.ok(pacoteProntoAssembler.collectionToModel(pacoteRepository.buscarPacoteProntoAtivo()));
    }

    @GetMapping(value = "/{pacoteProntoId}")
    public ResponseEntity<PacoteProntoDto> pacoteProntoPorId(@PathVariable Long pacoteProntoId){
        return ResponseEntity.ok(pacoteProntoAssembler.toModel(pacoteProntoService.buscarPorId(pacoteProntoId)));
    }

    @PostMapping
    public ResponseEntity<PacoteProntoDto> criarPacotePronto(@RequestBody PacoteProntoInput pacoteProntoInput){
        PacotePronto pacotePronto = pacoteProntoDissembler.toDomainObject(pacoteProntoInput);
        PacotePronto paconteProntoSalvo = pacoteProntoService.criarPacotePronto(pacotePronto);
        return ResponseEntity.ok(pacoteProntoAssembler.toModel(paconteProntoSalvo));
    }





}
