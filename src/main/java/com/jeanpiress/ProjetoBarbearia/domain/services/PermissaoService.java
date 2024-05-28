package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.exceptions.PermissaoNaoEncontradaException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Permissao;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PermissaoService {


    @Autowired
    private PermissaoRepository permissaoRepository;


    public Permissao buscarPorId(Long permissaoId){
       return permissaoRepository.findById(permissaoId).
                orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
    }

}
