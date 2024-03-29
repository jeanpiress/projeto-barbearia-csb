package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.api.controller.converteDto.assebler.ProdutoAssembler;
import com.jeanpiress.ProjetoBarbaria.api.controller.converteDto.assebler.dissembler.ProdutoInputDissembler;
import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.ProdutoDto;
import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.input.ProdutoInput;
import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import com.jeanpiress.ProjetoBarbaria.domain.services.CategoriaService;
import com.jeanpiress.ProjetoBarbaria.domain.services.ProdutoService;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.ProdutoRepository;
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

    @Autowired
    private ProdutoAssembler produtoAssembler;

    @Autowired
    private ProdutoInputDissembler produtoDissembler;

    @GetMapping
    public ResponseEntity<List<ProdutoDto>> listar(){
        List<Produto> produtos = repository.findAll();
        List<ProdutoDto> produtosDto = produtoAssembler.collectionToModel(produtos);
        return ResponseEntity.ok(produtosDto);
    }

    @GetMapping(value = "/{produtoId}")
    public ResponseEntity<ProdutoDto> buscarPorId(@PathVariable Long produtoId) {
        Produto produto = service.buscarPorId(produtoId);
        ProdutoDto produtoDto = produtoAssembler.toModel(produto);
        return ResponseEntity.ok(produtoDto);
    }

    @PostMapping
    public ResponseEntity<ProdutoDto> adicionar(@RequestBody ProdutoInput produtoInput) {
        Produto produto = produtoDissembler.toDomainObject(produtoInput);
        Produto produtoCriado = service.adicionar(produto);
        ProdutoDto produtoDto = produtoAssembler.toModel(produtoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoDto);
    }

    @DeleteMapping("/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long produtoId){
        service.remover(produtoId);

    }

}
