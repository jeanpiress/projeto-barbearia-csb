package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ProfissionalNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbaria.domain.services.ProfissionalService;
import com.jeanpiress.ProjetoBarbaria.repositories.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalRepository repository;

    @Autowired
    private ProfissionalService service;

    @GetMapping
    public ResponseEntity<List<Profissional>> listar(){
        List<Profissional> profissionais = repository.findAll();
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping(value = "/{profissionalId}")
    public ResponseEntity<Profissional> buscarPorId(@PathVariable Long profissionalId) {
        Profissional profissional = service.buscarPorId(profissionalId);
        return ResponseEntity.ok(profissional);
    }

    @PostMapping
    public ResponseEntity<Profissional> adicionar(@RequestBody Profissional profissional) {
        Profissional profissionalCriado = service.adicionar(profissional);
        return ResponseEntity.status(HttpStatus.CREATED).body(profissionalCriado);
    }

    @DeleteMapping("/{profissionalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long profissionalId){
        service.remover(profissionalId);

    }

}
