package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ItemPedidoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbaria.repositories.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ItemPedidoService {

    private static final String MSG_ITEM_EM_USO = "Item de código %d não pode ser removido, pois esta em uso";
    @Autowired
    private ItemPedidoRepository repository;

    @Transactional
    public ItemPedido adicionar(ItemPedido itemPedido) {
        return repository.save(itemPedido);
    }

    @Transactional
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
}
