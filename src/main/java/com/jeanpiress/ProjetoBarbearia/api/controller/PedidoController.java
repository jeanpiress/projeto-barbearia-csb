package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.PedidoControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.PedidoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.PedidoInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.PedidoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.PedidoAlteracaoInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.PedidoInput;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.FormaPagamentoJson;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.RealizacaoItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbearia.domain.model.Pedido;
import com.jeanpiress.ProjetoBarbearia.domain.services.ItemPedidoService;
import com.jeanpiress.ProjetoBarbearia.domain.services.PedidoService;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path ="/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController implements PedidoControllerOpenApi {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoAssembler pedidoAssembler;

    @Autowired
    private PedidoInputDissembler pedidoDissembler;
    @Autowired
    private ItemPedidoService itemPedidoService;


    //@PreAuthorize("hasAuthority('RECEPCAO')")
    @GetMapping
    public ResponseEntity<List<PedidoDto>> listarPagosCaixaAberto(){
        List<Pedido> pedidos = pedidoRepository.findByPagoAndCaixaAberto();
        List<PedidoDto> pedidosDto = pedidoAssembler.collectionToModel(pedidos);
        return ResponseEntity.ok(pedidosDto);
    }

    //@PreAuthorize("hasAuthority('RECEPCAO')")
    @GetMapping("/aguardando")
    public ResponseEntity<List<PedidoDto>> listarPedidosAguardando(){
        List<Pedido> pedidos = pedidoRepository.findByAguardando();
        List<PedidoDto> pedidosDto = pedidoAssembler.collectionToModel(pedidos);
        return ResponseEntity.ok(pedidosDto);
    }

    //@PreAuthorize("hasAuthority('RECEPCAO')")
    @GetMapping("/emAtendimento")
    public ResponseEntity<List<PedidoDto>> listarPedidosEmAtendimento(){
        List<Pedido> pedidos = pedidoRepository.findByEmAtendimento();
        List<PedidoDto> pedidosDto = pedidoAssembler.collectionToModel(pedidos);
        return ResponseEntity.ok(pedidosDto);
    }

    //@PreAuthorize("hasAuthority('RECEPCAO')")
    @GetMapping(value = "/{pedidoId}")
    public ResponseEntity<PedidoDto> buscarPorId(@PathVariable Long pedidoId) {
        Pedido pedido = pedidoService.buscarPorId(pedidoId);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedido);
        return ResponseEntity.ok(pedidoDto);
    }

    //@PreAuthorize("hasAuthority('CLIENTE')")
    @PostMapping
    public ResponseEntity<PedidoDto> adicionar(@RequestBody @Valid PedidoInput pedidoInput) {
        Pedido pedido = pedidoDissembler.toDomainObject(pedidoInput);
        Pedido pedidoCriado = pedidoService.criar(pedido);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedidoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDto);

    }

    //@PreAuthorize("hasAuthority('CLIENTE')")
    @DeleteMapping("cancelar/{pedidoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable Long pedidoId){
        pedidoService.cancelarPedido(pedidoId);

    }

    //@PreAuthorize("hasAuthority('CLIENTE')")
    @DeleteMapping("excluir/{pedidoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long pedidoId){
        pedidoService.excluirPedido(pedidoId);

    }

    //@PreAuthorize("hasAuthority('CLIENTE')")
    @DeleteMapping("limpar-itens")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void limparItens(@PathVariable Long pedidoId){
        pedidoService.excluirPedido(pedidoId);

    }

    //@PreAuthorize("hasAuthority('RECEPCAO')")
    @PutMapping("/{pedidoId}/add-item/{itemPedidoId}")
    public ResponseEntity<PedidoDto> adicionarItemPedido(@PathVariable Long pedidoId, @PathVariable Long itemPedidoId){
        Pedido pedido = pedidoService.adicionarItemPedido(pedidoId, itemPedidoId);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedido);
        return ResponseEntity.ok(pedidoDto);
    }

    //@PreAuthorize("hasAuthority('RECEPCAO')")
    @PutMapping(value = "/{pedidoId}")
    public ResponseEntity<PedidoDto> alterar(@RequestBody @Valid PedidoAlteracaoInput pedidoAlteracaoInput, @PathVariable Long pedidoId) {
        Pedido pedido = pedidoService.alterarProfissionalOuHorarioPedido(pedidoAlteracaoInput, pedidoId);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedidoRepository.save(pedido));
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDto);

    }

    //@PreAuthorize("hasAuthority('RECEPCAO')")
    @PutMapping(value = "/{pedidoId}/profissional/{profissionalId}")
    public ResponseEntity<PedidoDto> alterarProfissional(@PathVariable Long pedidoId, @PathVariable Long profissionalId) {
        Pedido pedido = pedidoService.alterarProfissional(profissionalId, pedidoId);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedidoRepository.save(pedido));

        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDto);

    }


    //@PreAuthorize("hasAuthority('RECEPCAO')")
    @DeleteMapping("/{pedidoId}/remove-item/{itemPedidoId}")
    public ResponseEntity<PedidoDto> removerItemPedido(@PathVariable Long pedidoId, @PathVariable Long itemPedidoId){
        Pedido pedido = pedidoService.removerItemPedido(pedidoId, itemPedidoId);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedido);
        return ResponseEntity.ok(pedidoDto);
    }

    //@PreAuthorize("hasAuthority('RECEPCAO')")
    @DeleteMapping("/{pedidoId}/remove-todos-itens")
    public ResponseEntity<PedidoDto> removerTodosItemPedido(@PathVariable Long pedidoId){
        Pedido pedidoOriginal = pedidoService.buscarPorId(pedidoId);
        //List<Long> intensPedidoId = pedidoOriginal.getItemPedidos().stream().map(ItemPedido::getId).toList();
        Pedido pedidoAlterado = pedidoService.removerTodosItensPedido(pedidoOriginal);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedidoAlterado);
        return ResponseEntity.ok(pedidoDto);
    }


    //@PreAuthorize("hasAuthority('RECEPCAO')")
    @PutMapping(value = "/{pedidoId}/pagar")
    public ResponseEntity<PedidoDto> efetuarPagamento(@RequestBody @Valid FormaPagamentoJson formaPagamento, @PathVariable @Valid Long pedidoId) {
       Pedido pedido = pedidoService.realizarPagamento(formaPagamento, pedidoId);
       PedidoDto pedidoDto = pedidoAssembler.toModel(pedido);
       return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDto);

    }

    //@PreAuthorize("hasAuthority('RECEPCAO')")
    @PutMapping(value = "/{pedidoId}/pagar/pacote")
    public ResponseEntity<PedidoDto> efetuarPagamentoComPacote(@RequestBody @Valid RealizacaoItemPacote realizacaoItemPacote, @PathVariable @Valid Long pedidoId) {
        Pedido pedido = pedidoService.realizarPagamentoComPedidoExistente(realizacaoItemPacote, pedidoId);
        PedidoDto pedidoDto = pedidoAssembler.toModel(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDto);

    }

    //@PreAuthorize("hasAuthority('PROFISSIONAL')")
    @PutMapping(value = "/{pedidoId}/confirmar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmarPedido(@PathVariable Long pedidoId){
        pedidoService.confirmarPedido(pedidoId);
    }

    //@PreAuthorize("hasAuthority('PROFISSIONAL')")
    @PutMapping(value = "/{pedidoId}/iniciar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void iniciarExecucaoPedido(@PathVariable Long pedidoId){
        pedidoService.iniciarExecucaoPedido(pedidoId);
    }

}
