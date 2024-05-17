package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.CategoriaControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.CategoriaAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.CategoriaInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.CategoriaDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.CategoriaInput;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.CategoriaNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.NegocioException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbearia.domain.services.CategoriaService;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping(path ="/categorias", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoriaController implements CategoriaControllerOpenApi {

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
    public ResponseEntity<CategoriaDto> adicionar(@RequestBody @Valid CategoriaInput categoriaInput) {
        Categoria categoria = categoriaDissembler.toDomainObject(categoriaInput);
        Categoria categoriaCriada = service.adicionar(categoria);
        CategoriaDto categoriaDto = categoriaAssembler.toModel(categoriaCriada);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaDto);
    }

    @PutMapping(value = "/{categoriaId}")
    public ResponseEntity<CategoriaDto> alterar(@RequestBody @Valid CategoriaInput categoriaInput, @PathVariable Long categoriaId) {
        try {
            Categoria categoria = service.buscarPorId(categoriaId);
            categoriaDissembler.copyToDomainObject(categoriaInput, categoria);
            CategoriaDto categoriaDto = categoriaAssembler.toModel(service.adicionar(categoria));
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaDto);
        }catch(CategoriaNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{categoriaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long categoriaId){
        service.remover(categoriaId);

    }

}
