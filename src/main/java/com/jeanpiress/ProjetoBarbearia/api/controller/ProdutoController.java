package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.ProdutoControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ProdutoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.ProdutoInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ProdutoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ProdutoInput;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ProdutoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.NegocioException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;
import com.jeanpiress.ProjetoBarbearia.domain.services.ProdutoService;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping(path ="/produtos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProdutoController implements ProdutoControllerOpenApi {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private ProdutoService service;

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

    @GetMapping(value = "/categoria/{categoriaId}")
    public ResponseEntity<List<ProdutoDto>> buscarPorCategoria(@PathVariable Long categoriaId){
        List<Produto> produtos = repository.buscarPorCategoria(categoriaId);
        List<ProdutoDto> produtosDto = produtoAssembler.collectionToModel(produtos);
        return ResponseEntity.ok(produtosDto);
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
        Produto produto = service.buscarPorId(produtoId);
        produtoDissembler.copyToDomainObject(produtoInput, produto);
        ProdutoDto produtoDto = produtoAssembler.toModel(service.adicionar(produto));
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoDto);

    }

    @DeleteMapping("/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long produtoId){
        service.remover(produtoId);

    }

}
