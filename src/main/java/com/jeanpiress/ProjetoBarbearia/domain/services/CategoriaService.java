package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.exceptions.CategoriaNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private static final String MSG_CATEGORIA_EM_USO = "Categoria de código %d não pode ser removido, pois esta em uso";
    @Autowired
    private CategoriaRepository repository;

    public Categoria buscarPorId(Long categoriaId){
       return repository.findById(categoriaId).
                orElseThrow(() -> new CategoriaNaoEncontradoException(categoriaId));
    }


    public Categoria adicionar(Categoria categoria) {
        return repository.save(categoria);
    }


    public void remover(Long categoriaId) {
        try {
            repository.deleteById(categoriaId);
        }catch (EmptyResultDataAccessException e) {
            throw new CategoriaNaoEncontradoException(categoriaId);
        }catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_CATEGORIA_EM_USO, categoriaId));
        }
    }
}
