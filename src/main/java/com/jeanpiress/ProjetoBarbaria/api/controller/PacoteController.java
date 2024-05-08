package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.api.converteDto.assebler.PacoteAssembler;
import com.jeanpiress.ProjetoBarbaria.api.converteDto.dissembler.PacoteInputDissembler;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.PacoteDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.PacoteInput;
import com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao.RealiazacaoItemPacote;
import com.jeanpiress.ProjetoBarbaria.domain.model.Pacote;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.PacoteRepository;
import com.jeanpiress.ProjetoBarbaria.domain.services.PacoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController()
@RequestMapping(path = "/pacotes", produces = MediaType.APPLICATION_JSON_VALUE)
public class PacoteController {

    @Autowired
    private PacoteRepository pacoteRepository;

    @Autowired
    private PacoteService pacoteService;

    @Autowired
    private PacoteAssembler pacoteAssembler;

    @Autowired
    private PacoteInputDissembler pacoteDissembler;

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

    @PostMapping
    public ResponseEntity<PacoteDto> criarPacoteFinal(@RequestBody @Valid PacoteInput pacoteInput){
        Pacote pacoteSalvo = pacoteService.criarPacoteFinal(pacoteInput);
        return ResponseEntity.ok(pacoteAssembler.toModel(pacoteSalvo));
    }

    @PutMapping("/receber-pacote")
    public ResponseEntity<PacoteDto> receberPacote(@RequestBody @Valid RealiazacaoItemPacote realizacaoItemPacote){
        Pacote pacote = pacoteService.receberPacote(realizacaoItemPacote);

        return ResponseEntity.ok(pacoteAssembler.toModel(pacote));
    }





}
