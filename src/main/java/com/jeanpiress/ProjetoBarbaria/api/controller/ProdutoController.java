package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.api.converteDto.assebler.ProdutoAssembler;
import com.jeanpiress.ProjetoBarbaria.api.converteDto.dissembler.ProdutoInputDissembler;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.ProdutoDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.ProdutoInput;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ProdutoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.NegocioException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import com.jeanpiress.ProjetoBarbaria.domain.services.ProdutoService;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private ProdutoService service;

    @Autowired
    private ProdutoService produtoService;

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
    public ResponseEntity<ProdutoDto> adicionar(@RequestBody @Valid ProdutoInput produtoInput) {
        Produto produto = produtoDissembler.toDomainObject(produtoInput);
        Produto produtoCriado = service.adicionar(produto);
        ProdutoDto produtoDto = produtoAssembler.toModel(produtoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoDto);
    }

    @PutMapping(value = "/{produtoId}")
    public ResponseEntity<ProdutoDto> alterar(@RequestBody @Valid ProdutoInput produtoInput, @PathVariable Long produtoId) {
        try {
            Produto produto = service.buscarPorId(produtoId);
            produtoDissembler.copyToDomainObject(produtoInput, produto);
            ProdutoDto produtoDto = produtoAssembler.toModel(service.adicionar(produto));
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoDto);
        }catch(ProdutoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long produtoId){
        service.remover(produtoId);

    }

}
