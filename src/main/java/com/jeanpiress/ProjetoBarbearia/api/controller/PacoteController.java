package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.PacoteControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.PacoteAssembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.PacoteDto;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.RealiazacaoItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.model.Pacote;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PacoteRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.PacoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController()
@RequestMapping(path = "/pacotes", produces = MediaType.APPLICATION_JSON_VALUE)
public class PacoteController implements PacoteControllerOpenApi {

    @Autowired
    private PacoteRepository pacoteRepository;

    @Autowired
    private PacoteService pacoteService;

    @Autowired
    private PacoteAssembler pacoteAssembler;


    @GetMapping
    public ResponseEntity<List<PacoteDto>> listar(){
        List<Pacote> pacotes = pacoteRepository.findAll();
        return ResponseEntity.ok(pacoteAssembler.collectionToModel(pacotes));
    }

    @GetMapping(value = "/{pacoteId}")
    public ResponseEntity<PacoteDto> buscarPorId(@PathVariable Long pacoteId){
        Pacote pacote = pacoteService.buscarPorId(pacoteId);
        return ResponseEntity.ok(pacoteAssembler.toModel(pacote));
    }

    @GetMapping(value = "/ativos")
    public ResponseEntity<List<PacoteDto>> buscarPacotesAtivos(){
        List<Pacote> pacotes = pacoteService.buscarPacotesComItensAtivos();
        return ResponseEntity.ok(pacoteAssembler.collectionToModel(pacotes));
    }

    @GetMapping(value = "/expirados")
    public ResponseEntity<List<PacoteDto>> buscarPacotesExpirados(){
        List<Pacote> pacotes = pacoteService.buscarPacotesComItensExpirados();
        return ResponseEntity.ok(pacoteAssembler.collectionToModel(pacotes));
    }

    @GetMapping(value = "/cliente/{clienteId}")
    public ResponseEntity<List<PacoteDto>> buscarPorCliente(@PathVariable Long clienteId){
        List<Pacote> pacotes = pacoteService.buscarPorClinte(clienteId);
        return ResponseEntity.ok(pacoteAssembler.collectionToModel(pacotes));
    }


    @PutMapping("/receber-pacote")
    public ResponseEntity<PacoteDto> receberPacote(@RequestBody @Valid RealiazacaoItemPacote realizacaoItemPacote){
        Pacote pacote = pacoteService.receberPacote(realizacaoItemPacote);

        return ResponseEntity.ok(pacoteAssembler.toModel(pacote));
    }





}
