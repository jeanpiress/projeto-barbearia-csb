package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ProdutoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import com.jeanpiress.ProjetoBarbaria.domain.services.CategoriaService;
import com.jeanpiress.ProjetoBarbaria.domain.services.ProdutoService;
import com.jeanpiress.ProjetoBarbaria.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private ProdutoService service;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Produto>> listar(){
        List<Produto> produtoList = repository.findAll();
        return ResponseEntity.ok(produtoList);
    }

    @GetMapping(value = "/{produtoId}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long produtoId) {
        Produto produto = service.buscarPorId(produtoId);

        return ResponseEntity.ok(produto);
    }

    @PostMapping
    public ResponseEntity<Produto> adicionar(@RequestBody Produto produto) {
        Long categoriaId = produto.getCategoria().getId();
        Categoria catagoria = categoriaService.buscarPorId(categoriaId);
        produto.setCategoria(catagoria);
        Produto produtoCriado = service.adicionar(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
    }

    @DeleteMapping("/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long produtoId){
        service.remover(produtoId);

    }

}
