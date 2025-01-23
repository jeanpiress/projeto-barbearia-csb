package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.UsuarioControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.FotoUsuarioAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.UsuarioAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.UsuarioInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.FotoUsuarioDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.UsuarioDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.FotoInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.UsuarioInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.UsuarioNovaSenhaInput;
import com.jeanpiress.ProjetoBarbearia.core.security.CsbSecurity;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.EntidadeNaoEncontradaException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.SenhaNaoConfirmadaException;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.FotoUsuarioRepository;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.UsuarioRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.FotoStorageService;
import com.jeanpiress.ProjetoBarbearia.domain.services.FotoUsuarioService;
import com.jeanpiress.ProjetoBarbearia.domain.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
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

   @Autowired
   private FotoUsuarioAssembler fotoUsuarioAssembler;

    @Autowired
    private FotoUsuarioService fotoUsuarioService;

    @Autowired
    private FotoStorageService fotoStorageService;

    private static final String DIRETORIO = "usuarios";

    @Autowired
    private FotoUsuarioRepository fotoUsuarioRepository;

    @Autowired
    private CsbSecurity security;

   @PreAuthorize("hasAuthority('GERENTE')")
   @GetMapping
   public ResponseEntity<List<UsuarioDto>> buscarUsuarios(){
      List<Usuario> usuarios = usuarioRepository.findAll();
      return ResponseEntity.ok(usuarioAssembler.collectionToModel(usuarios));
   }

   @PreAuthorize("hasAuthority('GERENTE')")
   @GetMapping(value = "/{usuarioId}")
   public ResponseEntity<UsuarioDto> buscarUsuarioPorId(@PathVariable Long usuarioId){
       Usuario usuario = usuarioService.buscarPorId(usuarioId);
       return ResponseEntity.ok(usuarioAssembler.toModel(usuario));
   }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping(value = "ocupacao/{id}")
    public UsuarioDto buscarUsuarioPorIdProfissionalCliente(@PathVariable Long id,
                                                            @RequestParam String ocupacao){

        Usuario usuario = usuarioService.buscarPorIdProfissionalCliente(id, ocupacao);
        return usuarioAssembler.toModel(usuario);
    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @GetMapping(value = "/email/{email}")
    public ResponseEntity<UsuarioDto> buscarUsuarioPorEmail(@PathVariable String email){
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(usuarioAssembler.toModel(usuario));
    }

    @PostMapping(value = "/criar/cliente/{clienteId}")
    public ResponseEntity<UsuarioDto> criarUsuarioClienteExistente(@RequestBody @Valid UsuarioInput usuarioInput, @PathVariable @Valid Long clienteId){
        usuarioService.confimacaoSenha(usuarioInput);
        Usuario usuario = usuarioDissembler.toDomainObject(usuarioInput);
        Usuario usuarioNovo = usuarioService.criarUsuarioClienteExistente(usuario, clienteId);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAssembler.toModel(usuarioNovo));
    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @PostMapping(value = "/criar")
    public ResponseEntity<UsuarioDto> criarUsuarioNovo(@RequestBody @Valid UsuarioInput usuarioInput){
        usuarioService.confimacaoSenha(usuarioInput);
        Usuario usuario = usuarioDissembler.toDomainObject(usuarioInput);
        Usuario usuarioNovo = usuarioService.criarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAssembler.toModel(usuarioNovo));
    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @PostMapping(value = "/criar/profissional/{profissionalId}")
    public ResponseEntity<UsuarioDto> criarUsuarioProfissionalExistente(@RequestBody @Valid UsuarioInput usuarioInput, @PathVariable @Valid Long profissionalId){
        usuarioService.confimacaoSenha(usuarioInput);
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

    @PreAuthorize("hasAuthority('CLIENTE')")
    @PutMapping(value = "/{usuarioId}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FotoUsuarioDto atualizarFoto(@PathVariable Long usuarioId, @Valid FotoInput fotoInput) throws IOException {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        if(!security.temAutorizacao("GERENTE") && !security.getUsuarioId().equals(usuarioId)) {
            throw new AccessDeniedException("");
        }

            MultipartFile arquivo = fotoInput.getArquivo();

            FotoUsuario foto = FotoUsuario.builder()
                    .usuario(usuario)
                    .descricao(fotoInput.getDescricao())
                    .contentType(arquivo.getContentType())
                    .tamanho(arquivo.getSize())
                    .nomeArquivo(arquivo.getOriginalFilename())
                    .build();

            return fotoUsuarioAssembler.toModel(fotoUsuarioService.salvar(foto, arquivo.getInputStream(), DIRETORIO));
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping(value = "/{produtoId}/foto", produces = MediaType.APPLICATION_JSON_VALUE)
    public FotoUsuarioDto buscarFoto(@PathVariable Long produtoId){
        FotoUsuario fotoUsuario = fotoUsuarioService.buscarbyId(produtoId);


        return fotoUsuarioAssembler.toModel(fotoUsuario);
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping(value = "/{produtoId}/foto", produces = MediaType.ALL_VALUE)
    public ResponseEntity<?> servirFoto(@PathVariable Long produtoId, @RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {
        try {
            FotoUsuario fotoUsuario = fotoUsuarioService.buscarbyId(produtoId);

            MediaType mediaTypeFoto = MediaType.parseMediaType(fotoUsuario.getContentType());
            List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);

            fotoStorageService.verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);

            FotoRecuperada fotoRecuperada = fotoStorageService.recuperar(fotoUsuario.getNomeArquivo(), DIRETORIO);

            if(fotoRecuperada.temUrl()){
                return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, fotoRecuperada.getUrl()).build();
            }else{
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(fotoRecuperada.getInputStream()));
            }
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{produtoId}/foto")
    public void deletarFoto(@PathVariable Long produtoId){
        FotoUsuario fotoUsuario = fotoUsuarioService.buscarbyId(produtoId);
        fotoUsuarioRepository.delete(fotoUsuario);
        fotoStorageService.remover(fotoUsuario.getNomeArquivo(), DIRETORIO);
    }

}
