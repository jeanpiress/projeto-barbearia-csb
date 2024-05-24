package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ItemPacoteNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbearia.domain.model.PacotePronto;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ItemPacoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemPacoteService {


    @Autowired
    private ItemPacoteRepository repository;

    @Autowired
    ItemPedidoService itemPedidoService;


    public ItemPacote buscarPorId(Long itemPacoteId){
        return repository.findById(itemPacoteId).
                orElseThrow(() -> new ItemPacoteNaoEncontradoException(itemPacoteId));
    }

    //cria um novo item pacote baseado no que foi passado, com novo id, sem data de consumo e sem profissional a partir de uma lista de itens pacote
    public List<ItemPacote> criarNovosItensPacoteRecebendoListaItemPacote(List<ItemPacote> itensPacote) {
        List<Long> itensPacoteId = itensPacote.stream()
                .map(ItemPacote::getId).collect(Collectors.toList());

        List<ItemPedido> itensPedido = itemPedidoService.buscarListaComIds(itensPacoteId);

        return criarNovosItensPacote(itensPedido);
    }

    //cria um novo item pacote baseado no que foi passado, com novo id, sem data de consumo e sem profissional a partir de um pacote pronto
    public List<ItemPacote> criarNovosItensPacoteRecebendoPacotePronto(PacotePronto pacotePronto) {
        List<ItemPacote> itensAtivos = pacotePronto.getItensAtivos();
        List<Long> itensPacoteId = itensAtivos.stream().map(ItemPacote::getId).collect(Collectors.toList());

        List<ItemPedido> itensPedido = itemPedidoService.buscarListaComIds(itensPacoteId);

        return criarNovosItensPacote(itensPedido);
    }

    private List<ItemPacote> criarNovosItensPacote(List<ItemPedido> itensPedido){
        List<ItemPacote> itensPacoteSalvo= new ArrayList<>();
        for(ItemPedido itemPedido : itensPedido){
            ItemPacote itemPacote = ItemPacote.builder()
                    .itemPedido(itemPedido)
                    .build();
            itensPacoteSalvo.add(itemPacote);
        }
        return repository.saveAll(itensPacoteSalvo);
    }


}
