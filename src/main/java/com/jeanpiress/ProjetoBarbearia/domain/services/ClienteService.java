package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.eventos.ClienteAtendidoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ClienteNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClienteService {

    private static final String MSG_CLIENTE_EM_USO = "Cliente de código %d não pode ser removido, pois esta em uso";
    @Autowired
    private ClienteRepository repository;

    public Cliente buscarPorId(Long clienteId){
        return repository.findById(clienteId).
                orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));
    }

    public Cliente adicionar(Cliente cliente) {
        return repository.save(cliente);
    }

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

    public void desativar(Long clienteId) {
        Cliente cliente = buscarPorId(clienteId);
        cliente.setAtivo(false);
        repository.save(cliente);
    }

    //Escutando de PedidoService metodos realizarPagamentoComPacote e realizarPagamento
    @EventListener
    public void alterarPrevisaoRetorno(ClienteAtendidoEvento clienteAtendido){
        try{
            Cliente cliente = clienteAtendido.getCliente();
            Integer DiasRetorno = cliente.getDiasRetorno();
            LocalDate previsaoRetorno = LocalDate.now().plusDays(DiasRetorno);
            cliente.setPrevisaoRetorno(previsaoRetorno);

            repository.save(cliente);
        }catch (Exception e) {
            throw new ClienteNaoEncontradoException(clienteAtendido.getCliente().getId());
        }


    }
}
