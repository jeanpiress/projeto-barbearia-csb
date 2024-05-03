package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.PacoteNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.PacotePronto;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.PacoteProntoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PacoteProntoService {

    @Autowired
    private PacoteProntoRepository pacoteProntoRepository;

    @Autowired
    private PacoteService pacoteService;


    public PacotePronto buscarPorId(Long pacoteProntoId) {
        return pacoteProntoRepository.findById(pacoteProntoId).
                orElseThrow(() -> new PacoteNaoEncontradoException(pacoteProntoId));

    }

    public List<PacotePronto> buscarPacotesAtivos() {
        return pacoteProntoRepository.buscarPacoteProntoAtivo();
    }


    public PacotePronto criarPacotePronto(PacotePronto pacotePronto) {
       pacotePronto.setItensAtivos(pacoteService.criarItensPacote(pacotePronto.getItensAtivos()));

        return pacoteProntoRepository.save(pacotePronto);
    }


}
