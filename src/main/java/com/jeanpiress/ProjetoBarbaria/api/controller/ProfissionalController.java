package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.api.converteDto.assebler.ProfissionalAssembler;
import com.jeanpiress.ProjetoBarbaria.api.converteDto.dissembler.ProfissionalInputDissembler;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.ProfissionalDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.ProfissionalInput;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ProfissionalNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.NegocioException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbaria.domain.services.ProfissionalService;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalRepository repository;

    @Autowired
    private ProfissionalService service;

    @Autowired
    private ProfissionalAssembler profissionalAssembler;

    @Autowired
    private ProfissionalInputDissembler profissionalDissembler;

    @GetMapping
    public ResponseEntity<List<ProfissionalDto>> listar(){
        List<Profissional> profissionais = repository.findAll();
        List<ProfissionalDto> profissionaisDto = profissionalAssembler.collectionToModel(profissionais);
        return ResponseEntity.ok(profissionaisDto);
    }

    @GetMapping(value = "/{profissionalId}")
    public ResponseEntity<ProfissionalDto> buscarPorId(@PathVariable Long profissionalId) {
        Profissional profissional = service.buscarPorId(profissionalId);
        ProfissionalDto profissionalDto = profissionalAssembler.toModel(profissional);
        return ResponseEntity.ok(profissionalDto);
    }

    @PostMapping
    public ResponseEntity<ProfissionalDto> adicionar(@RequestBody @Valid ProfissionalInput profissionalInput) {
        Profissional profissional = profissionalDissembler.toDomainObject(profissionalInput);
        Profissional profissionalCriado = service.adicionar(profissional);
        ProfissionalDto profissionalDto = profissionalAssembler.toModel(profissionalCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(profissionalDto);
    }

    @PutMapping(value = "/{profissionalId}")
    public ResponseEntity<ProfissionalDto> alterar(@RequestBody @Valid ProfissionalInput profissionalInput, @PathVariable Long profissionalId) {
        try {
            Profissional profissional = service.buscarPorId(profissionalId);
            profissionalDissembler.copyToDomainObject(profissionalInput, profissional);
            ProfissionalDto profissionalDto = profissionalAssembler.toModel(service.adicionar(profissional));
            return ResponseEntity.status(HttpStatus.CREATED).body(profissionalDto);
        }catch(ProfissionalNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{profissionalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long profissionalId){
        service.remover(profissionalId);

    }

}
