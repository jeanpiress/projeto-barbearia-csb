package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.ProdutoControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.FotoProdutoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ProdutoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.ProdutoInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.FotoProdutoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ProdutoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.FotoProdutoInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ProdutoInput;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.EntidadeNaoEncontradaException;
import com.jeanpiress.ProjetoBarbearia.domain.model.FotoProduto;
import com.jeanpiress.ProjetoBarbearia.domain.model.FotoRecuperada;
import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.FotoProdutoRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.FotoProdutoService;
import com.jeanpiress.ProjetoBarbearia.domain.services.FotoStorageService;
import com.jeanpiress.ProjetoBarbearia.domain.services.ProdutoService;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController()
@RequestMapping(path ="/produtos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProdutoController implements ProdutoControllerOpenApi {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private ProdutoService service;

    @Autowired
    private ProdutoAssembler produtoAssembler;

    @Autowired
    private ProdutoInputDissembler produtoDissembler;

    @Autowired
    private FotoProdutoService fotoProdutoService;

    @Autowired
    private FotoProdutoAssembler fotoProdutoAssembler;

    @Autowired
    private FotoStorageService fotoStorageService;
    @Autowired
    private FotoProdutoRepository fotoProdutoRepository;

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping
    public ResponseEntity<List<ProdutoDto>> listar(@RequestParam String nome,
                                                   @RequestParam boolean isAtivo,
                                                   @RequestParam (required = false) Long categoriaId ){
        List<Produto> produtos = repository.findByNome(nome, isAtivo, categoriaId);
        List<ProdutoDto> produtosDto = produtoAssembler.collectionToModel(produtos);
        return ResponseEntity.ok(produtosDto);
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping(value = "/{produtoId}")
    public ResponseEntity<ProdutoDto> buscarPorId(@PathVariable Long produtoId) {
        Produto produto = service.buscarPorId(produtoId);
        ProdutoDto produtoDto = produtoAssembler.toModel(produto);
        return ResponseEntity.ok(produtoDto);
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping(value = "/categoria/{categoriaId}")
    public ResponseEntity<List<ProdutoDto>> buscarPorCategoria(@PathVariable Long categoriaId){
        List<Produto> produtos = repository.buscarPorCategoria(categoriaId);
        List<ProdutoDto> produtosDto = produtoAssembler.collectionToModel(produtos);
        return ResponseEntity.ok(produtosDto);
    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @PostMapping
    public ResponseEntity<ProdutoDto> adicionar(@RequestBody @Valid ProdutoInput produtoInput) {
        Produto produto = produtoDissembler.toDomainObject(produtoInput);
        Produto produtoCriado = service.adicionar(produto);
        ProdutoDto produtoDto = produtoAssembler.toModel(produtoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoDto);
    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @PutMapping(value = "/{produtoId}")
    public ResponseEntity<ProdutoDto> alterar(@RequestBody @Valid ProdutoInput produtoInput, @PathVariable Long produtoId) {
        Produto produto = service.buscarPorId(produtoId);
        produtoDissembler.copyToDomainObject(produtoInput, produto);
        ProdutoDto produtoDto = produtoAssembler.toModel(service.adicionar(produto));
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoDto);

    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @DeleteMapping("/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long produtoId){
        service.remover(produtoId);

    }

    @PreAuthorize("hasAuthority('GERENTE')")
    @DeleteMapping("/{produtoId}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long produtoId){
        service.desativar(produtoId);

    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @PutMapping(value = "/{produtoId}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FotoProdutoDto atualizarFoto(@PathVariable Long produtoId, @Valid FotoProdutoInput fotoProdutoInput) throws IOException {
        Produto produto = service.buscarPorId(produtoId);

        MultipartFile arquivo = fotoProdutoInput.getArquivo();

        FotoProduto foto = FotoProduto.builder()
                .produto(produto)
                .descricao(fotoProdutoInput.getDescricao())
                .contentType(arquivo.getContentType())
                .tamanho(arquivo.getSize())
                .nomeArquivo(arquivo.getOriginalFilename())
                .build();

        return fotoProdutoAssembler.toModel(fotoProdutoService.salvar(foto, arquivo.getInputStream()));
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping(value = "/{produtoId}/foto", produces = MediaType.APPLICATION_JSON_VALUE)
    public FotoProdutoDto buscarFoto(@PathVariable Long produtoId){
        FotoProduto fotoProduto = fotoProdutoService.buscarbyId(produtoId);


        return fotoProdutoAssembler.toModel(fotoProduto);
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping(value = "/{produtoId}/foto", produces = MediaType.ALL_VALUE)
    public ResponseEntity<?> servirFoto(@PathVariable Long produtoId, @RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {
        try {
            FotoProduto fotoProduto = fotoProdutoService.buscarbyId(produtoId);

            MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
            List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);

            verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);

            FotoRecuperada fotoRecuperada = fotoStorageService.recuperar(fotoProduto.getNomeArquivo());

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
        FotoProduto fotoProduto = fotoProdutoService.buscarbyId(produtoId);
        fotoProdutoRepository.delete(fotoProduto);
        fotoStorageService.remover(fotoProduto.getNomeArquivo());
    }

    private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {
        boolean compativel = mediaTypesAceitas.stream()
                .anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));

        if (!compativel) {
            throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
        }

    }

}
