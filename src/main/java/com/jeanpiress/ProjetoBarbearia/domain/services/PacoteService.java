package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.RealizacaoItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.eventos.PacoteRealizadoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.*;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ClienteRepository;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ItemPacoteRepository;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PacoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class PacoteService {

    @Autowired
    private PacoteRepository pacoteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;


    public Pacote buscarPorId(Long pacoteId) {
        return pacoteRepository.findById(pacoteId).
                orElseThrow(() -> new PacoteNaoEncontradoException(pacoteId));

    }

    public List<Pacote> buscarPorClinte(Long clienteId){
        if(!clienteRepository.existsById(clienteId)){
            throw new ClienteNaoEncontradoException(clienteId);
        }
        return pacoteRepository.findByClienteId(clienteId);
    }

    public List<Pacote> buscarPacotesComItensAtivos(){
        return pacoteRepository.findAllComItensAtivos();

    }

    public List<Pacote> buscarPacotesComItensExpirados(){
        return pacoteRepository.findAllComItensExpirados();

    }

    public Pacote adicionar(Pacote pacote) {
        return pacoteRepository.save(pacote);
    }

    //O EventPublisher e escutado por  criarPedidoPorPacote em PedidoService, e lá ele atualiza o pacote
    public Pacote realizarUmItemDoPacote(RealizacaoItemPacote realizacaoItemPacote) {
        Long itemPacoteId = realizacaoItemPacote.getItemPacote().getId();
        Long pacoteId = realizacaoItemPacote.getPacote().getId();
        Long profissionalId = realizacaoItemPacote.getProfissional().getId();

        if(!pacoteRepository.existsByIdAndItensAtivosId(pacoteId, itemPacoteId)) {
            throw new ItemPacoteNaoEncontradoEmItemAtivoException(itemPacoteId);
        }

        Pacote pacote = buscarPorId(pacoteId);
        Profissional profissional = profissionalService.buscarPorId(profissionalId);

        alterarAtivoParaConsumido(pacote, profissional, itemPacoteId);

        eventPublisher.publishEvent(new PacoteRealizadoEvento(pacote));

        return pacote;
    }

    public void alterarAtivoParaConsumido(Pacote pacote, Profissional profissional, Long itemPacoteId){
        List<ItemPacote> itensAtivos = pacote.getItensAtivos();
        List<ItemPacote> itensConsumidos = pacote.getItensConsumidos();

        if(itensAtivos.isEmpty()){
            throw new PacoteNaoPossuiItensAtivosException(pacote.getId());
        }

        ItemPacote itemAtivo =  itensAtivos.stream().filter(ativo -> ativo.getId().equals(itemPacoteId)).findFirst().get();
        itemAtivo.setDataConsumo(OffsetDateTime.now());
        itemAtivo.setProfissional(profissional);

        itensAtivos.remove(itemAtivo);
        pacote.setItensAtivos(itensAtivos);

        itensConsumidos.add(itemAtivo);
        pacote.setItensConsumidos(itensConsumidos);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // Executa todos os dias à meia-noite
     public void verificarValidadePacote() {
        List<Pacote> pacotes = buscarPacotesComItensAtivos();

        for(Pacote pacote : pacotes){
            OffsetDateTime dataVencimento = pacote.getDataVencimento();
            if (dataVencimento.isBefore(OffsetDateTime.now())) {
                List<ItemPacote> itensAtivos = pacote.getItensAtivos();
                List<ItemPacote> listaVazia = new ArrayList<>();
                List<ItemPacote> intensExpirados = pacote.getItensExpirados();

                for (ItemPacote itemAtivo : itensAtivos) {
                    itemAtivo.setDataConsumo(OffsetDateTime.now());
                    intensExpirados.add(itemAtivo);
                }

                pacote.setItensAtivos(listaVazia);
                pacote.setItensExpirados(intensExpirados);
            }
        }
        pacoteRepository.saveAll(pacotes);
    }

}
