package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.CategoriaNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbaria.domain.services.CategoriaService;
import com.jeanpiress.ProjetoBarbaria.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaService service;

    @GetMapping
    public ResponseEntity<List<Categoria>> listar(){
        List<Categoria> categoriaList = categoriaRepository.findAll();
        return ResponseEntity.ok(categoriaList);
    }

    @GetMapping(value = "/{categoriaId}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId).
                orElseThrow(() -> new CategoriaNaoEncontradoException(categoriaId));

        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<Categoria> adicionar(@RequestBody Categoria categoria) {
        Categoria categoriaCriada = service.adicionar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCriada);
    }

    @DeleteMapping("/{categoriaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long categoriaId){
        service.remover(categoriaId);

    }

}
