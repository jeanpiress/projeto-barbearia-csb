package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ItemPacoteNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ItemPacoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemPacoteService {


    @Autowired
    private ItemPacoteRepository repository;


    public ItemPacote buscarPorId(Long itemPacoteId){
        return repository.findById(itemPacoteId).
                orElseThrow(() -> new ItemPacoteNaoEncontradoException(itemPacoteId));
    }



}
