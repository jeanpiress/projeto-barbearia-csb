package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ProfissionalNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class ProfissionalService {

    private static final String MSG_PROFISSIONAL_EM_USO = "Profissional de código %d não pode ser removido, pois esta em uso";
    @Autowired
    private ProfissionalRepository repository;

    public Profissional buscarPorId(Long profissionalId){
        return repository.findById(profissionalId).
                orElseThrow(() -> new ProfissionalNaoEncontradoException(profissionalId));
    }

    public Profissional adicionar(Profissional profissional) {
        return repository.save(profissional);
    }


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

}
