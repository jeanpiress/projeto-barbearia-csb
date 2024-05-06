package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.PacoteInput;
import com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao.RealiazacaoItemPacote;
import com.jeanpiress.ProjetoBarbaria.domain.eventos.ClienteAtendidoEvento;
import com.jeanpiress.ProjetoBarbaria.domain.eventos.PacoteRealizadoEvento;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ClienteNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ItemPacoteNaoEncontradoEmItemAtivoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.PacoteNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.*;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.ClienteRepository;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.ItemPacoteRepository;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.ItemPedidoRepository;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.PacoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PacoteService {

    @Autowired
    private PacoteRepository pacoteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ItemPacoteRepository itemPacoteRepository;

    @Autowired
    private ItemPacoteService itemPacoteService;

    @Autowired
    private ItemPedidoService itemPedidoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PacoteProntoService pacoteProntoService;

    @Autowired
    private PacoteService pacoteService;

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

    public Pacote criarPacoteFinal(PacoteInput pacoteinput) {
        Cliente cliente = clienteService.buscarPorId(pacoteinput.getCliente().getId());
        PacotePronto pacotePronto = pacoteProntoService.buscarPorId(pacoteinput.getPacotePronto().getId());
        List<ItemPacote> itensPacote = criarNovosItensPacote(pacotePronto);

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

    public List<ItemPacote> criarItensPacote(List<ItemPacote> itensPacote) {
        List<ItemPacote> itensPacoteSalvo= new ArrayList<>();
        List<Long> itensPacoteId = itensPacote.stream()
                .map(ItemPacote::getId).collect(Collectors.toList());

        for(Long itemPacoteid : itensPacoteId){
            ItemPedido itemPedido = itemPedidoService.buscarPorId(itemPacoteid);
            ItemPacote itemPacote = ItemPacote.builder()
                    .itemPedido(itemPedido)
                    .build();
            itemPacoteRepository.save(itemPacote);
            itensPacoteSalvo.add(itemPacote);
        }

        return itensPacoteSalvo;
    }

    private List<ItemPacote> criarNovosItensPacote(PacotePronto pacotePronto) {
        List<ItemPacote> itensPacoteSalvo= new ArrayList<>();
        List<ItemPacote> itensAtivos = pacotePronto.getItensAtivos();

        for(ItemPacote itemAtivo : itensAtivos){
            Long itemPacoteid = itemAtivo.getItemPedido().getId();
            ItemPedido itemPedido = itemPedidoService.buscarPorId(itemPacoteid);
            ItemPacote itemPacote = ItemPacote.builder()
                    .itemPedido(itemPedido)
                    .build();
            itemPacoteRepository.save(itemPacote);
            itensPacoteSalvo.add(itemPacote);
        }

        return itensPacoteSalvo;
    }

    private OffsetDateTime calcularVencimentoPacote(Integer validade) {
        OffsetDateTime dataAtual = OffsetDateTime.now();
        OffsetDateTime dataVencimento = dataAtual.plusDays(validade);
        dataVencimento = dataVencimento.withHour(23).withMinute(59).withSecond(59).withNano(0);

        return dataVencimento;
    }


    public Pacote receberPacote(RealiazacaoItemPacote realizacaoItemPacote) {
        Long itemPacoteId = realizacaoItemPacote.getItemPacote().getId();
        Long pacoteId = realizacaoItemPacote.getPacote().getId();
        Long profissionalId = realizacaoItemPacote.getProfissional().getId();

        if(!pacoteRepository.existsByIdAndItensAtivosId(pacoteId, itemPacoteId)) {
            throw new ItemPacoteNaoEncontradoEmItemAtivoException(itemPacoteId);
        }

        Pacote pacote = pacoteService.buscarPorId(pacoteId);
        Profissional profissional = profissionalService.buscarPorId(profissionalId);

        List<ItemPacote> itensAtivos = pacote.getItensAtivos();
        List<ItemPacote> itensConsumidos = pacote.getItensConsumidos();

        ItemPacote itemAtivo =  itensAtivos.stream().filter(ativo -> ativo.getId().equals(itemPacoteId)).findFirst().get();
        itemAtivo.setDataConsumo(OffsetDateTime.now());
        itemAtivo.setProfissional(profissional);

        itensAtivos.remove(itemAtivo);
        pacote.setItensAtivos(itensAtivos);

        itensConsumidos.add(itemAtivo);
        pacote.setItensConsumidos(itensConsumidos);

        pacote.getItensConsumidos();


        eventPublisher.publishEvent(new PacoteRealizadoEvento(pacote));

        return pacote;
    }


}
