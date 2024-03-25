package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ComissaoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Comissao;
import com.jeanpiress.ProjetoBarbaria.domain.services.ComissaoService;
import com.jeanpiress.ProjetoBarbaria.repositories.ComissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/comissoes")
public class ComissaoController {

    @Autowired
    private ComissaoRepository repository;

    @Autowired
    private ComissaoService service;

    @GetMapping
    public ResponseEntity<List<Comissao>> listar(){
        List<Comissao> comissaoList = repository.findAll();
        return ResponseEntity.ok(comissaoList);
    }

    @GetMapping(value = "/{comissaoId}")
    public ResponseEntity<Comissao> buscarPorId(@PathVariable Long comissaoId) {
        Comissao comissao = repository.findById(comissaoId).
                orElseThrow(() -> new ComissaoNaoEncontradoException(comissaoId));

        return ResponseEntity.ok(comissao);
    }

  /*  @PostMapping
    public ResponseEntity<Comissao> adicionar(@RequestBody Comissao comissao) {
        Comissao comissaoCriado = service.adicionar(comissao);
        return ResponseEntity.status(HttpStatus.CREATED).body(comissaoCriado);
    }
*/
    @DeleteMapping("/{comissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long comissaoId){
        service.remover(comissaoId);

    }

}
