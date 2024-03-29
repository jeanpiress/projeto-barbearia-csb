package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.CategoriaNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ProfissionalNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
public class ProfissionalService {

    private static final String MSG_PROFISSIONAL_EM_USO = "Profissional de código %d não pode ser removido, pois esta em uso";
    @Autowired
    private ProfissionalRepository repository;

    public Profissional buscarPorId(Long profissionalId){
        return repository.findById(profissionalId).
                orElseThrow(() -> new CategoriaNaoEncontradoException(profissionalId));
    }
    @Transactional
    public Profissional adicionar(Profissional profissional) {
        return repository.save(profissional);
    }

    @Transactional
    public void remover(Long profissionalId) {
        try {
            repository.deleteById(profissionalId);
        }catch (EmptyResultDataAccessException e) {
            throw new ProfissionalNaoEncontradoException(profissionalId);
        }catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_PROFISSIONAL_EM_USO, profissionalId));
        }
    }

    public Set<Long> buscarIdProfissionaisAtivos(){
       return repository.buscarIdProfissionaisAtivos();
    }
}
