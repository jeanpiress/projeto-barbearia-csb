package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.api.controller.openapi.UsuarioControllerOpenApi;
import com.jeanpiress.ProjetoBarbaria.api.converteDto.assebler.UsuarioAssembler;
import com.jeanpiress.ProjetoBarbaria.api.converteDto.dissembler.UsuarioInputDissembler;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.UsuarioDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.UsuarioInput;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.UsuarioNovaSenhaInput;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.EmailExistenteException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.SenhaAtualIncorretaException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.UsuarioNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Permissao;
import com.jeanpiress.ProjetoBarbaria.domain.model.Usuario;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.UsuarioRepository;
import com.jeanpiress.ProjetoBarbaria.domain.services.PermissaoService;
import com.jeanpiress.ProjetoBarbaria.domain.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping(path = "/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioController implements UsuarioControllerOpenApi {

   @Autowired
   private UsuarioService usuarioService;

   @Autowired
   private UsuarioRepository usuarioRepository;

   @Autowired
   private UsuarioAssembler usuarioAssembler;

   @Autowired
   private UsuarioInputDissembler usuarioDissembler;

   @PreAuthorize("hasAuthority('GERENTE')")
   @GetMapping
   public ResponseEntity<List<UsuarioDto>> buscarUsuarios(){
       List<Usuario> usuarios = usuarioRepository.findAll();
       return ResponseEntity.ok(usuarioAssembler.collectionToModel(usuarios));
   }

   @PreAuthorize("hasAuthority('GERENTE')")
   @GetMapping(value = "/id/{usuarioId}")
   public ResponseEntity<UsuarioDto> buscarUsuarioPorId(@PathVariable Long usuarioId){
       Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId);
       return ResponseEntity.ok(usuarioAssembler.toModel(usuario));
   }

    @PreAuthorize("hasAuthority('GERENTE')")
    @GetMapping(value = "/email/{email}")
    public ResponseEntity<UsuarioDto> buscarUsuarioPorEmail(@PathVariable String email){
        Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);
        return ResponseEntity.ok(usuarioAssembler.toModel(usuario));
    }

    @PostMapping(value = "/criar/cliente/{clienteId}")
    public ResponseEntity<UsuarioDto> criarUsuarioClienteExistente(@RequestBody @Valid UsuarioInput usuarioInput, @PathVariable @Valid Long clienteId){
        Usuario usuario = usuarioDissembler.toDomainObject(usuarioInput);
        if(usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new EmailExistenteException();
        }
        Set<Permissao> permissoes = usuarioService.permissoesInferiores(4L);
        usuario.setPermissoes(permissoes);
        Usuario usuarioNovo = usuarioService.criarUsuarioClienteExistente(usuario, clienteId);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAssembler.toModel(usuarioNovo));
    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @PostMapping(value = "/criar")
    public ResponseEntity<UsuarioDto> criarUsuarioNovo(@RequestBody @Valid UsuarioInput usuarioInput){
        Usuario usuario = usuarioDissembler.toDomainObject(usuarioInput);
        if(usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new EmailExistenteException();
        }
        Long permissao = usuarioInput.getPermissao();
        Set<Permissao> permissoes = usuarioService.permissoesInferiores(permissao);
        usuario.setPermissoes(permissoes);
        Usuario usuarioNovo = usuarioService.criarUsuario(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAssembler.toModel(usuarioNovo));
    }

    @PreAuthorize("hasAuthority('RECEPCAO')")
    @PostMapping(value = "/criar/profissional/{profissionalId}")
    public ResponseEntity<UsuarioDto> criarUsuarioProfissionalExistente(@RequestBody @Valid UsuarioInput usuarioInput, @PathVariable @Valid Long profissionalId){
        Usuario usuario = usuarioDissembler.toDomainObject(usuarioInput);
        if(usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new EmailExistenteException();
        }
        Long permissaoId = usuarioInput.getPermissao();
        Set<Permissao> permissoes = usuarioService.permissoesInferiores(permissaoId);
        usuario.setPermissoes(permissoes);
        Usuario usuarioNovo = usuarioService.criarUsuarioProfissionalExistente(usuario, profissionalId);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAssembler.toModel(usuarioNovo));
    }


    @PutMapping(value = "/alterar-senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@RequestBody @Valid UsuarioNovaSenhaInput novaSenha){
        Usuario usuarioConferido = usuarioService.conferirSenha(novaSenha);

    }
    @PreAuthorize("hasAuthority('GERENTE')")
    @PutMapping(value = "/{usuarioId}/alterar-permissao/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarPermissaoUsuario(@PathVariable Long usuarioId, @PathVariable Long permissaoId){
       usuarioService.alterarPermissao(usuarioId, permissaoId);
    }



}
