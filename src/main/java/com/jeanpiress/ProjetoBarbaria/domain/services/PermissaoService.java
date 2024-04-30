package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.PermissaoNaoEncontradaException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.UsuarioNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.model.Permissao;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbaria.domain.model.Usuario;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.PermissaoRepository;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PermissaoService {


    @Autowired
    private PermissaoRepository permissaoRepository;


    public Permissao buscarPermissaoPorid(Long permissaoId){
       return permissaoRepository.findById(permissaoId).
                orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
    }

}
