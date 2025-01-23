package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ProfissionalInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.UsuarioInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.UsuarioNovaSenhaInput;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.*;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


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


    public Usuario buscarPorId(Long usuarioId){
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
    }

    public Usuario buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());
    }

    public Usuario buscarPorIdProfissionalCliente(Long id, String ocupacao){

        Long profissionalId = null;
        Long clienteId = null;

        if(ocupacao.equals("PROFISSIONAL")){
            profissionalId = id;
        }else if(ocupacao.equals("CLIENTE")){
            clienteId = id;
        }else {
            throw new HttpMessageNotReadableException("Mensagem incompreensivel");
        }

        return usuarioRepository.findByIdProfissionalCliente(profissionalId,clienteId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());
    }

    public Usuario criarUsuarioClienteExistente(Usuario usuario, Long clienteId){
        verificarSeEmailExistente(usuario);
        Cliente cliente = clienteService.buscarPorId(clienteId);
        usuario.setCliente(cliente);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setPermissoes(permissoesInferiores(4L));
        usuario.setMaiorPermissao("CLIENTE");

        return usuarioRepository.save(usuario);
    }

    public Usuario criarUsuario(Usuario usuario){
        verificarSeEmailExistente(usuario);
        if(usuario.getMaiorPermissao() == null){
            usuario.setMaiorPermissao("CLIENTE");
        }
        String permissao = usuario.getMaiorPermissao();
        Long permissaoId = buscarIdPermissaoPorNome(permissao.toUpperCase());
        Set<Permissao> permissoes = permissoesInferiores(permissaoId);
        usuario.setPermissoes(permissoes);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }


    public Usuario criarUsuarioProfissionalExistente(Usuario usuario, Long profissionalId){
        verificarSeEmailExistente(usuario);
        String permissao = usuario.getMaiorPermissao();
        Long permissaoId = buscarIdPermissaoPorNome(permissao.toUpperCase());
        Set<Permissao> permissoes = permissoesInferiores(permissaoId);
        usuario.setPermissoes(permissoes);
        Profissional profissional = profissionalService.buscarPorId(profissionalId);
        usuario.setProfissional(profissional);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        return usuarioRepository.save(usuario);
    }

    private Set<Permissao> permissoesInferiores(Long permissaoId) {
        Set<Permissao> permissoes = new HashSet<>();

        while(permissaoId <= 4){
            Permissao permissao = permissaoService.buscarPorId(permissaoId);
            permissoes.add(permissao);
            permissaoId++;

        }

        return permissoes;
    }

    public void alterarSenha(UsuarioNovaSenhaInput novaSenha){
        Usuario usuario = buscarPorEmail(novaSenha.getEmail());

        if(!passwordEncoder.matches(novaSenha.getSenhaAtual(), usuario.getSenha())){
            throw new SenhaAtualIncorretaException();
        }

        if(!novaSenha.getNovaSenha().equals(novaSenha.getConfirmarSenha())){
            throw new ConferenciaSenhaException();
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha.getNovaSenha()));

        usuarioRepository.save(usuario);
    }

    public void alterarPermissao(Long usuarioId, Long permissaoId) {
        Usuario usuario = buscarPorId(usuarioId);
        Set<Permissao> permissoes = permissoesInferiores(permissaoId);
        usuario.setPermissoes(permissoes);
        Permissao maiorPermissao = permissoes.stream()
                .min(Comparator.comparing(Permissao::getId))
                .get();
        usuario.setMaiorPermissao(maiorPermissao.getNome());
        usuarioRepository.save(usuario);
    }

    public Long buscarIdPermissaoPorNome(String permissao){
        switch (permissao) {
            case "GERENTE": return 1L;
            case "RECEPCAO": return 2L;
            case "PROFISSIONAL": return 3L;
            default: throw new PermissaoInvalidaException(String.format("%s não é um nome de permissao valido", permissao));
        }
    }

    private void verificarSeEmailExistente(Usuario usuario){
        if(usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new EmailExistenteException();
        }
    }

    public void confimacaoSenha(UsuarioInput usuarioInput){
        if(!usuarioInput.getSenha().equals(usuarioInput.getConfirmacao())){
            throw new SenhaNaoConfirmadaException();
        }
    }
}
