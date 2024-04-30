package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.api.controller.openapi.PedidoControllerOpenApi;
import com.jeanpiress.ProjetoBarbaria.api.converteDto.assebler.PedidoAssembler;
import com.jeanpiress.ProjetoBarbaria.api.converteDto.dissembler.PedidoInputDissembler;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.PedidoDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.PedidoInput;
import com.jeanpiress.ProjetoBarbaria.core.security.CsbSecurity;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.StatusPedido;
import com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao.FormaPagamentoJson;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.PedidoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.NegocioException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Pedido;
import com.jeanpiress.ProjetoBarbaria.domain.model.Usuario;
import com.jeanpiress.ProjetoBarbaria.domain.services.PedidoService;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.PedidoRepository;
import com.jeanpiress.ProjetoBarbaria.domain.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping(path ="/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController implements PedidoControllerOpenApi {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private PedidoService service;

    @Autowired
    private PedidoAssembler pedidoAssembler;

    @Autowired
    private PedidoInputDissembler pedidoDissembler;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CsbSecurity security;
    @Autowired
    private PedidoRepository pedidoRepository;

    @PreAuthorize("hasAuthority('RECEPCAO')")
    @GetMapping
    public ResponseEntity<List<PedidoDto>> listar(){
        List<Pedido> pedidos = repository.findByPagoAndCaixaAberto();
        List<PedidoDto> pedidosDto = pedidoAssembler.collectionToModel(pedidos);
        return ResponseEntity.ok(pedidosDto);
    }

    @PreAuthorize("hasAuthority('RECEPCAO')")
    @GetMapping(value = "/{pedidoId}")
    public ResponseEntity<PedidoDto> buscarPorId(@PathVariable Long pedidoId) {
        Pedido pedido = service.buscarPorId(pedidoId);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedido);

        return ResponseEntity.ok(pedidoDto);
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @PostMapping
    public ResponseEntity<PedidoDto> adicionar(@RequestBody @Valid PedidoInput pedidoInput) {
        Pedido pedido = pedidoDissembler.toDomainObject(pedidoInput);
        pedido.setCriadoAs(OffsetDateTime.now());
        Usuario usuario = usuarioService.buscarUsuarioPorId(security.getUsuarioId());
        pedido.setCriadoPor(usuario);
        Pedido pedidoCriado = service.criar(pedido);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedidoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDto);

    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @DeleteMapping("/{pedidoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable Long pedidoId){
        Pedido pedido = service.buscarPorId(pedidoId);
        pedido.setStatusPedido(StatusPedido.CANCELADO);
        pedido.setCanceladoAs(OffsetDateTime.now());
        Usuario usuario = usuarioService.buscarUsuarioPorId(security.getUsuarioId());
        pedido.setCanceladoPor(usuario);
        pedidoRepository.save(pedido);

    }

    @PreAuthorize("hasAuthority('RECEPCAO')")
    @PutMapping("/{pedidoId}/add-item/{itemPedidoId}")
    public ResponseEntity<PedidoDto> adicionarItemPedido(@PathVariable Long pedidoId, @PathVariable Long itemPedidoId){
        Pedido pedido = service.adicionarItemPedido(pedidoId, itemPedidoId);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedido);
        return ResponseEntity.ok(pedidoDto);
    }

    @PreAuthorize("hasAuthority('RECEPCAO')")
    @PutMapping(value = "/{pedidoId}")
    public ResponseEntity<PedidoDto> alterar(@RequestBody @Valid PedidoInput pedidoInput, @PathVariable Long pedidoId) {
        try {
            Pedido pedido = service.buscarPorId(pedidoId);
            pedidoDissembler.copyToDomainObject(pedidoInput, pedido);
            Usuario usuario = usuarioService.buscarUsuarioPorId(security.getUsuarioId());
            pedido.setAlteradoPor(usuario);
            pedido.setModificadoAs(OffsetDateTime.now());
            PedidoDto pedidoDto = pedidoAssembler.toModel(repository.save(pedido));
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDto);
        }catch(PedidoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }


    @PreAuthorize("hasAuthority('RECEPCAO')")
    @DeleteMapping("/{pedidoId}/remove-item/{itemPedidoId}")
    public ResponseEntity<PedidoDto> removerItemPedido(@PathVariable Long pedidoId, @PathVariable Long itemPedidoId){
        Pedido pedido = service.removerItemPedido(pedidoId, itemPedidoId);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedido);
        return ResponseEntity.ok(pedidoDto);
    }


    @PreAuthorize("hasAuthority('RECEPCAO')")
    @PutMapping(value = "/{pedidoId}/pagar")
    public ResponseEntity<PedidoDto> efetuarPagamento(@RequestBody @Valid FormaPagamentoJson formaPagamento, @PathVariable @Valid Long pedidoId) {
        try {
            Pedido pedido = service.realizarPagamento(formaPagamento, pedidoId);
            Usuario usuario = usuarioService.buscarUsuarioPorId(security.getUsuarioId());
            pedido.setRecibidoPor(usuario);
            PedidoDto pedidoDto = pedidoAssembler.toModel(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDto);
        }catch(PedidoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }



}
