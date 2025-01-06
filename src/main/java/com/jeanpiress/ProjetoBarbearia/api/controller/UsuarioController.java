package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.UsuarioControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.UsuarioAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.UsuarioInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.UsuarioDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.UsuarioInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.UsuarioNovaSenhaInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.Usuario;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.UsuarioRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
       Usuario usuario = usuarioService.buscarPorId(usuarioId);
       return ResponseEntity.ok(usuarioAssembler.toModel(usuario));
   }

    @PreAuthorize("hasAuthority('GERENTE')")
    @GetMapping(value = "/email/{email}")
    public ResponseEntity<UsuarioDto> buscarUsuarioPorEmail(@PathVariable String email){
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(usuarioAssembler.toModel(usuario));
    }

    @PostMapping(value = "/criar/cliente/{clienteId}")
    public ResponseEntity<UsuarioDto> criarUsuarioClienteExistente(@RequestBody @Valid UsuarioInput usuarioInput, @PathVariable @Valid Long clienteId){
        Usuario usuario = usuarioDissembler.toDomainObject(usuarioInput);
        Usuario usuarioNovo = usuarioService.criarUsuarioClienteExistente(usuario, clienteId);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAssembler.toModel(usuarioNovo));
    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @PostMapping(value = "/criar")
    public ResponseEntity<UsuarioDto> criarUsuarioNovo(@RequestBody @Valid UsuarioInput usuarioInput){
        Usuario usuario = usuarioDissembler.toDomainObject(usuarioInput);
        Usuario usuarioNovo = usuarioService.criarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAssembler.toModel(usuarioNovo));
    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @PostMapping(value = "/criar/profissional/{profissionalId}")
    public ResponseEntity<UsuarioDto> criarUsuarioProfissionalExistente(@RequestBody @Valid UsuarioInput usuarioInput, @PathVariable @Valid Long profissionalId){
        Usuario usuario = usuarioDissembler.toDomainObject(usuarioInput);
        Usuario usuarioNovo = usuarioService.criarUsuarioProfissionalExistente(usuario, profissionalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAssembler.toModel(usuarioNovo));
    }


    @PutMapping(value = "/alterar-senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@RequestBody @Valid UsuarioNovaSenhaInput novaSenha){
        usuarioService.alterarSenha(novaSenha);

    }
    @PreAuthorize("hasAuthority('GERENTE')")
    @PutMapping(value = "/{usuarioId}/alterar-permissao/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarPermissaoUsuario(@PathVariable Long usuarioId, @PathVariable Long permissaoId){
       usuarioService.alterarPermissao(usuarioId, permissaoId);
    }



}
