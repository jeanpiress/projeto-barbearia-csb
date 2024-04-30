package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.UsuarioNovaSenhaInput;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ConferenciaSenhaException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.SenhaAtualIncorretaException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.UsuarioNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.*;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private PermissaoService permissaoService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Usuario buscarUsuarioPorId(Long usuarioId){
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
    }

    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());
    }


    public Usuario criarUsuarioClienteExistente(Usuario usuario, Long clienteId){
        Cliente cliente = clienteService.buscarPorId(clienteId);
        usuario.setCliente(cliente);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        maiorPermissao(usuario);

        return usuarioRepository.save(usuario);
    }

    public Usuario criarUsuario(Usuario usuario){
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }


    public Usuario criarUsuarioProfissionalExistente(Usuario usuario, Long profissionalId){
        Profissional profissional = profissionalService.buscarPorId(profissionalId);
        usuario.setProfissional(profissional);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        maiorPermissao(usuario);

        return usuarioRepository.save(usuario);
    }

    public Set<Permissao> permissoesInferiores(Long permissaoId) {
        Set<Permissao> permissoes = new HashSet<>();

        while(permissaoId <= 4){
            Permissao permissao = permissaoService.buscarPermissaoPorid(permissaoId);
            permissoes.add(permissao);
            permissaoId++;
        }
        return permissoes;
    }

    public Usuario conferirSenha(UsuarioNovaSenhaInput novaSenha){
        Usuario usuario = buscarUsuarioPorEmail(novaSenha.getEmail());


        if(!passwordEncoder.matches(novaSenha.getSenhaAtual(), usuario.getSenha())){
            throw new SenhaAtualIncorretaException();
        }

        if(!novaSenha.getNovaSenha().equals(novaSenha.getConfirmarSenha())){
            throw new ConferenciaSenhaException();
        }

        usuario.setSenha(novaSenha.getNovaSenha());

        return usuarioRepository.save(usuario);
    }

    private void maiorPermissao(Usuario usuario) {
        Set<Permissao> permissoes = usuario.getPermissoes();

        Optional<Permissao> permissaoComMenorId = permissoes.stream()
                .min(Comparator.comparingLong(Permissao::getId));

        usuario.setPermissao(permissaoComMenorId.get().getNome());

    }
}
