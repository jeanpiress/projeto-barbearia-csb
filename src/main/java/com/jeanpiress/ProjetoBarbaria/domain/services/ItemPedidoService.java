package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ItemPedidoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ItemPedidoService {

    private static final String MSG_ITEM_EM_USO = "Item de código %d não pode ser removido, pois esta em uso";
    @Autowired
    private ItemPedidoRepository repository;

    @Autowired
    private ProdutoService produtoService;


    public ItemPedido buscarPorId(Long itemPedidoId){
        return repository.findById(itemPedidoId).
                orElseThrow(() -> new ItemPedidoNaoEncontradoException(itemPedidoId));
    }

    public ItemPedido adicionar(ItemPedido itemPedido) {
        Long produtoId = itemPedido.getProduto().getId();
        Produto produto = produtoService.buscarPorId(produtoId);
        itemPedido.setProduto(produto);
        calcularValores(itemPedido);
        return repository.save(itemPedido);
    }


    public void remover(Long itemPedidoId) {
        try {
            repository.deleteById(itemPedidoId);
        }catch (EmptyResultDataAccessException e) {
            throw new ItemPedidoNaoEncontradoException(itemPedidoId);
        }catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_ITEM_EM_USO, itemPedidoId));
        }
    }

    public void calcularValores(ItemPedido itemPedido){
        Produto produto = itemPedido.getProduto();
        Integer quantidade = itemPedido.getQuantidade();
        BigDecimal valorUnitario = produto.getPreco();
        BigDecimal valorTotal =  valorUnitario.multiply(BigDecimal.valueOf(quantidade));

        itemPedido.setPrecoUnitario(produto.getPreco());
        itemPedido.setPrecoUnitario(valorUnitario);
        itemPedido.setPrecoTotal(valorTotal);

    }


}
