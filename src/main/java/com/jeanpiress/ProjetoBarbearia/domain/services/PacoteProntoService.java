package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.eventos.PacoteProntoCriadoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.PacoteNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PacoteProntoRepository;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PacoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;


@Service
public class PacoteProntoService {

    @Autowired
    private PacoteProntoRepository pacoteProntoRepository;

    @Autowired
    private PacoteService pacoteService;

    @Autowired
    private PacoteRepository pacoteRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ItemPacoteService itemPacoteService;


    public PacotePronto buscarPorId(Long pacoteProntoId) {
        return pacoteProntoRepository.findById(pacoteProntoId).
                orElseThrow(() -> new PacoteNaoEncontradoException(pacoteProntoId));

    }

    public List<PacotePronto> buscarPacotesAtivos() {
        return pacoteProntoRepository.buscarPacoteProntoAtivo();
    }


    public PacotePronto criarPacotePronto(PacotePronto pacotePronto) {
       pacotePronto.setItensAtivos(itemPacoteService.criarNovosItensPacoteRecebendoListaItemPacote(pacotePronto.getItensAtivos()));
       PacotePronto pacoteProntoSalvo = pacoteProntoRepository.save(pacotePronto);
       eventPublisher.publishEvent(new PacoteProntoCriadoEvento(pacoteProntoSalvo));
       return pacoteProntoSalvo;
    }



    public Pacote criarPacoteFinal(Long clienteId, Long pacoteProntoId) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        PacotePronto pacotePronto = buscarPorId(pacoteProntoId);
        List<ItemPacote> itensPacote = itemPacoteService.criarNovosItensPacoteRecebendoPacotePronto(pacotePronto);

        Pacote pacote = Pacote.builder()
                .cliente(cliente)
                .dataCompra(OffsetDateTime.now())
                .dataVencimento(calcularVencimentoPacote(pacotePronto.getValidade()))
                .itensAtivos(itensPacote)
                .validade(pacotePronto.getValidade())
                .nome(pacotePronto.getNome())
                .descricao(pacotePronto.getDescricao())
                .build();

        return pacoteRepository.save(pacote);
    }

    private OffsetDateTime calcularVencimentoPacote(Integer validade) {
        OffsetDateTime dataAtual = OffsetDateTime.now();
        OffsetDateTime dataVencimento = dataAtual.plusDays(validade);
        dataVencimento = dataVencimento.withHour(23).withMinute(59).withSecond(59).withNano(0);

        return dataVencimento;
    }

}
