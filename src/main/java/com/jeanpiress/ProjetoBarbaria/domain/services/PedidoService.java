package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.CategoriaNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.PedidoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbaria.domain.model.Pedido;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbaria.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PedidoService {

    private static final String MSG_PEDIDO_EM_USO = "Pedido de código %d não pode ser removido, pois esta em uso";
    @Autowired
    private PedidoRepository repository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ItemPedidoService itemPedidoService;

    @Autowired
    private ProfissionalService profissionalService;

    public Pedido buscarPorId(Long pedidoId){
        return repository.findById(pedidoId).
                orElseThrow(() -> new CategoriaNaoEncontradoException(pedidoId));
    }
    @Transactional
    public Pedido adicionar(Pedido pedido) {
        Cliente cliente = clienteService.buscarPorId(pedido.getCliente().getId());
        Profissional profissional = profissionalService.buscarPorId(pedido.getProfissional().getId());
        pedido.setCliente(cliente);
        pedido.setProfissional(profissional);
        return repository.save(pedido);
    }

    @Transactional
    public void remover(Long pedidoId) {
        try {
            repository.deleteById(pedidoId);
        }catch (EmptyResultDataAccessException e) {
            throw new PedidoNaoEncontradoException(pedidoId);
        }catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_PEDIDO_EM_USO, pedidoId));
        }
    }

    @Transactional
    public Pedido adicionarItemPedido(Long pedidoId, Long itemPedidoId){
        Pedido pedido = buscarPorId(pedidoId);
        ItemPedido itemPedido = itemPedidoService.buscarPorId(itemPedidoId);
        pedido.adicionarItemPedido(itemPedido);
        return pedido;
    }

    @Transactional
    public Pedido removerItemPedido(Long pedidoId, Long itemPedidoId){
        Pedido pedido = buscarPorId(pedidoId);
        ItemPedido itemPedido = itemPedidoService.buscarPorId(itemPedidoId);
        pedido.removerItemPedido(itemPedido);
        return pedido;
    }
}
