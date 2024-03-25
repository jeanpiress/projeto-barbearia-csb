package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ComissaoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Comissao;
import com.jeanpiress.ProjetoBarbaria.repositories.ComissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ComissaoService {

    private static final String MSG_COMISSAO_EM_USO = "Comissao de código %d não pode ser removido, pois esta em uso";
    @Autowired
    private ComissaoRepository repository;

    @Transactional
    public Comissao adicionar(Comissao comissao) {
        return repository.save(comissao);
    }

    @Transactional
    public void remover(Long comissaoId) {
        try {
            repository.deleteById(comissaoId);
        }catch (EmptyResultDataAccessException e) {
            throw new ComissaoNaoEncontradoException(comissaoId);
        }catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_COMISSAO_EM_USO, comissaoId));
        }
    }
}
