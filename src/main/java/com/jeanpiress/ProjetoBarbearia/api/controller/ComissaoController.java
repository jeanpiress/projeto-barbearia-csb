package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.ComissaoControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ComissaoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.ComissaoInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ComissaoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ComissaoInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.Comissao;
import com.jeanpiress.ProjetoBarbearia.domain.services.ComissaoService;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ComissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping(path ="/comissoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ComissaoController implements ComissaoControllerOpenApi {

    @Autowired
    private ComissaoRepository repository;

    @Autowired
    private ComissaoService service;

    @Autowired
    private ComissaoAssembler comissaoAssembler;

    @Autowired
    private ComissaoInputDissembler comissaoDissembler;

    @GetMapping
    public ResponseEntity<List<ComissaoDto>> listar(){
        List<Comissao> comissoes = repository.findAll();
        List<ComissaoDto> comissoesDto = comissaoAssembler.collectionToModel(comissoes);
        return ResponseEntity.ok(comissoesDto);
    }

    @GetMapping(value = "/{comissaoId}")
    public ResponseEntity<ComissaoDto> buscarPorId(@PathVariable Long comissaoId) {
        Comissao comissao = service.buscarPorId(comissaoId);
        ComissaoDto comissaoDto = comissaoAssembler.toModel(comissao);
        return ResponseEntity.ok(comissaoDto);
    }

    @PostMapping
    public ResponseEntity<ComissaoDto> adicionar(@RequestBody @Valid ComissaoInput comissaoInput) {
        Comissao comissao = comissaoDissembler.toDomainObject(comissaoInput);
        Comissao comissaoCriada = service.adicionar(comissao);
        ComissaoDto comissaoDto = comissaoAssembler.toModel(comissaoCriada);
        return ResponseEntity.status(HttpStatus.CREATED).body(comissaoDto);
    }

    @PutMapping(value = "/{comissaoId}")
    public ResponseEntity<ComissaoDto> alterar(@RequestBody @Valid ComissaoInput comissaoInput, @PathVariable Long comissaoId) {
        Comissao comissao = service.buscarPorId(comissaoId);
        comissaoDissembler.copyToDomainObject(comissaoInput, comissao);
        ComissaoDto comissaoDto = comissaoAssembler.toModel(service.adicionar(comissao));
        return ResponseEntity.status(HttpStatus.CREATED).body(comissaoDto);
    }

    @DeleteMapping("/{comissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long comissaoId){
        service.remover(comissaoId);

    }

}
