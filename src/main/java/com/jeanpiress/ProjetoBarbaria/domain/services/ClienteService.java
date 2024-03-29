package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.CategoriaNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ClienteNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ClienteService {

    private static final String MSG_CLIENTE_EM_USO = "Cliente de código %d não pode ser removido, pois esta em uso";
    @Autowired
    private ClienteRepository repository;

    public Cliente buscarPorId(Long clienteId){
        return repository.findById(clienteId).
                orElseThrow(() -> new CategoriaNaoEncontradoException(clienteId));
    }
    @Transactional
    public Cliente adicionar(Cliente cliente) {
        return repository.save(cliente);
    }

    @Transactional
    public void remover(Long clienteId) {
        try {
            repository.deleteById(clienteId);
        }catch (EmptyResultDataAccessException e) {
            throw new ClienteNaoEncontradoException(clienteId);
        }catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_CLIENTE_EM_USO, clienteId));
        }
    }
}
