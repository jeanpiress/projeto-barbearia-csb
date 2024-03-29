package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.api.controller.converteDto.assebler.CategoriaAssembler;
import com.jeanpiress.ProjetoBarbaria.api.controller.converteDto.assebler.dissembler.CategoriaInputDissembler;
import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.CategoriaDto;
import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.input.CategoriaInput;
import com.jeanpiress.ProjetoBarbaria.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbaria.domain.services.CategoriaService;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private CategoriaService service;

    @Autowired
    private CategoriaAssembler categoriaAssembler;

    @Autowired
    private CategoriaInputDissembler categoriaDissembler;

    @GetMapping
    public ResponseEntity<List<CategoriaDto>> listar(){
        List<Categoria> categorias = repository.findAll();
        List<CategoriaDto> categriasDto = categoriaAssembler.collectionToModel(categorias);
        return ResponseEntity.ok(categriasDto);
    }

    @GetMapping(value = "/{categoriaId}")
    public ResponseEntity<CategoriaDto> buscarPorId(@PathVariable @Valid Long categoriaId) {
        Categoria categoria = service.buscarPorId(categoriaId);
        CategoriaDto categoriaDto = categoriaAssembler.toModel(categoria);
        return ResponseEntity.ok(categoriaDto);
    }

    @PostMapping
    public ResponseEntity<CategoriaDto> adicionar(@RequestBody CategoriaInput categoriaInput) {
        Categoria categoria = categoriaDissembler.toDomainObject(categoriaInput);
        Categoria categoriaCriada = service.adicionar(categoria);
        CategoriaDto categoriaDto = categoriaAssembler.toModel(categoriaCriada);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaDto);
    }

    @DeleteMapping("/{categoriaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long categoriaId){
        service.remover(categoriaId);

    }

}
