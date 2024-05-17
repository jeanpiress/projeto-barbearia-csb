package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.PedidoAlteracaoInput;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.FormaPagamentoJson;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPedido;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.RealiazacaoItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.eventos.ClienteAtendidoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.eventos.PacoteRealizadoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.*;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PacoteRepository;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class PedidoService {

    private static final String MSG_PEDIDO_EM_USO = "Pedido de código %d não pode ser removido, pois esta em uso";
    @Autowired
    private PedidoRepository repository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ItemPedidoService itemPedidoService;

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private ComissaoService comissaoService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private PacoteRepository pacoteRepository;

    @Autowired
    private PacoteService pacoteService;


    @Autowired
    private ItemPacoteService itemPacoteService;
    @Autowired
    private PedidoRepository pedidoRepository;


    public Pedido buscarPorId(Long pedidoId){
        return repository.findById(pedidoId).
                orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
    }

    @EventListener
    public void criarPedidoPorPacote(PacoteRealizadoEvento pacoteRealizado) {
        Pacote pacote = pacoteRealizado.getPacote();
        Cliente cliente = pacote.getCliente();
        var itensConsumidos = pacote.getItensConsumidos();
        ItemPacote ultimoItemConsumido = itensConsumidos.stream().max(Comparator.comparing(ItemPacote::getDataConsumo)).get();
        Profissional profissional = ultimoItemConsumido.getProfissional();
        Pedido pedido = new Pedido();

        pedido.setCliente(cliente);
        pedido.setProfissional(profissional);
        pedido.setPontuacaoGerada(gerarPontuacao(ultimoItemConsumido.getItemPedido()));
        preencherPedidoPorPacote(pedido, ultimoItemConsumido);
        repository.save(pedido);
        pacoteRepository.save(pacote);
    }

    public Pedido criar(Pedido pedido) {
        Cliente cliente = clienteService.buscarPorId(pedido.getCliente().getId());
        Profissional profissional = profissionalService.buscarPorId(pedido.getProfissional().getId());
        pedido.setCliente(cliente);
        pedido.setProfissional(profissional);
        pedido.setPontuacaoGerada(BigDecimal.ZERO);
        preencherPedido(pedido);

        return repository.save(pedido);
    }


    public void remover(Long pedidoId) {
        try {
            repository.deleteById(pedidoId);
        }catch (EmptyResultDataAccessException e) {
            throw new PedidoNaoEncontradoException(pedidoId);
        }catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_PEDIDO_EM_USO, pedidoId));
        }
    }


    public Pedido adicionarItemPedido(Long pedidoId, Long itemPedidoId){
        Pedido pedido = buscarPorId(pedidoId);
        Long profissionalId = pedido.getProfissional().getId();
        ItemPedido itemPedido = itemPedidoService.buscarPorId(itemPedidoId);
        if(!pedido.getItemPedidos().contains(itemPedido)) {
            pedido.getItemPedidos().add(itemPedido);
            BigDecimal comissaoGerada = comissaoPorItem(profissionalId, itemPedido);
            BigDecimal comissaoPedido = pedido.getComissaoGerada().add(comissaoGerada);
            pedido.setComissaoGerada(comissaoPedido);
            BigDecimal valorTotal = pedido.getValorTotal().add(itemPedido.getPrecoTotal());
            pedido.setValorTotal(valorTotal);
            BigDecimal pontuacao = pedido.getPontuacaoGerada().add(gerarPontuacao(itemPedido));
            pedido.setPontuacaoGerada(pontuacao);
        }
        if(itemPedido.getProduto().getPacotePronto() != null){
           pacoteService.criarPacoteFinal(pedido.getCliente().getId(),
                   itemPedido.getProduto().getPacotePronto().getId());
        }

        return repository.save(pedido);
    }


    public Pedido removerItemPedido(Long pedidoId, Long itemPedidoId){
        Pedido pedido = buscarPorId(pedidoId);
        ItemPedido itemPedido = itemPedidoService.buscarPorId(itemPedidoId);
        Long profissionalId = pedido.getProfissional().getId();
        if(pedido.getItemPedidos().contains(itemPedido)) {
            pedido.removerItemPedido(itemPedido);
            itemPedidoService.remover(itemPedidoId);
            BigDecimal comissaoGerada = comissaoPorItem(profissionalId, itemPedido);
            BigDecimal comissaoPedido = pedido.getComissaoGerada().subtract(comissaoGerada);
            pedido.setComissaoGerada(comissaoPedido);
        }

        return repository.save(pedido);
    }

    public BigDecimal comissaoPorItem(Long profissionalId, ItemPedido itemPedido){
        Long produtoId = itemPedido.getProduto().getId();
        Comissao comissao = comissaoService.buscarPorProfissionalProduto(profissionalId, produtoId);
        BigDecimal comissaoProdutoUnitario = comissaoService.calculoComissaoProduto(comissao);
        Integer quantidade = itemPedido.getQuantidade();
        BigDecimal comissaoProdutoFinal = comissaoProdutoUnitario.multiply(new BigDecimal(quantidade));

        return  comissaoProdutoFinal;
    }

    public BigDecimal gerarPontuacao(ItemPedido itemPedido){
        BigDecimal pesoPontuacao = itemPedido.getProduto().getPesoPontuacaoProfissional();
        BigDecimal valorProduto = itemPedido.getProduto().getPreco();
        BigDecimal pontuacaProduto = valorProduto.multiply(pesoPontuacao);
        BigDecimal pontuacaoItem = pontuacaProduto.multiply(BigDecimal.valueOf(itemPedido.getQuantidade()));

        return  pontuacaoItem;
    }


    public void preencherPedido(Pedido pedido){
        pedido.setStatusPedido(StatusPedido.AGENDADO);
        pedido.setFormaPagamento(FormaPagamento.AGUARDANDO_PAGAMENTO);
        pedido.setStatusPagamento(StatusPagamento.AGUARDANDO_PAGAMENTO);
        pedido.setComissaoGerada(BigDecimal.ZERO);
        pedido.setValorTotal(BigDecimal.ZERO);

    }


    public Pedido realizarPagamentoComPacote(RealiazacaoItemPacote realizacaoItemPacote, Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        if(pedido.getStatusPagamento() == StatusPagamento.PAGO){
            throw new PedidoJaFoiPagoException(pedidoId);
        }

        Pacote pacote = pacoteService.buscarPorId(realizacaoItemPacote.getPacote().getId());

        if(pacote.getDataVencimento().isBefore(OffsetDateTime.now())){
            throw new PacoteVencidoException(realizacaoItemPacote.getPacote().getId());
        }

        Cliente clientePedido = pedido.getCliente();
        Cliente clientePacote = pacote.getCliente();
        if(!clientePacote.equals(clientePedido)){
            throw new ClientesDiferentesException("Cliente do pedido não é o titular do pacote");
        }
        Long itemPacoteId = realizacaoItemPacote.getItemPacote().getId();
        ItemPacote itemPacote = itemPacoteService.buscarPorId(itemPacoteId);

        if(!pacote.getItensAtivos().contains(itemPacote) && pacote.getItensConsumidos().contains(itemPacote)){
            throw new ItemPacoteNaoEncontradoEmItemAtivoException(String.format("O item de codigo %d já foi utlizado", itemPacoteId));
        }

        if(!pacote.getItensAtivos().contains(itemPacote) && !pacote.getItensConsumidos().contains(itemPacote)){
            throw new ItemPacoteNaoEncontradoEmItemAtivoException(itemPacoteId);
        }

        Profissional profissional = profissionalService.buscarPorId(realizacaoItemPacote.getProfissional().getId());

        pedido.setProfissional(profissional);
        pedido.setPontuacaoGerada(gerarPontuacao(itemPacote.getItemPedido()));
        preencherPedidoPorPacote(pedido, itemPacote);
        pacoteService.alterarArtivoParaConsumido(pacote, profissional, itemPacoteId);

        repository.save(pedido);
        pacoteRepository.save(pacote);

        return pedido;
    }

    private void preencherPedidoPorPacote(Pedido pedido, ItemPacote ultimoItemConsumido){
        List<ItemPedido> itensPedidos = pedido.getItemPedidos();
        ItemPedido itemPedido = ultimoItemConsumido.getItemPedido();
        itensPedidos.add(itemPedido);
        pedido.setItemPedidos(itensPedidos);
        pedido.setStatusPedido(StatusPedido.FINALIZADO);
        pedido.setFormaPagamento(FormaPagamento.VOUCHER);
        pedido.setStatusPagamento(StatusPagamento.PAGO);
        pedido.setComissaoGerada(comissaoPorItem(pedido.getProfissional().getId(), itemPedido));
        pedido.setValorTotal(itemPedido.getPrecoTotal());
        pedido.setDataPagamento(OffsetDateTime.now());

    }


    public Pedido realizarPagamento(FormaPagamentoJson formaPagamento, Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);

        if(formaPagamento.getFormaPagamento().equals("dinheiro")){
            pedido.setFormaPagamento(FormaPagamento.DINHEIRO);
        }
        if(formaPagamento.getFormaPagamento().equals("pix")){
            pedido.setFormaPagamento(FormaPagamento.PIX);
        }
        if(formaPagamento.getFormaPagamento().equals("debito")){
            pedido.setFormaPagamento(FormaPagamento.DEBITO);
        }
        if(formaPagamento.getFormaPagamento().equals("credito")){
            pedido.setFormaPagamento(FormaPagamento.CREDITO);
        }
        if(formaPagamento.getFormaPagamento().equals("voucher")){
            pedido.setFormaPagamento(FormaPagamento.VOUCHER);
        }
        if(formaPagamento.getFormaPagamento().equals("pontos")){
            pedido.setFormaPagamento(FormaPagamento.PONTO);
        }
        pedido.setDataPagamento(OffsetDateTime.now());

        pedido.setStatusPagamento(StatusPagamento.PAGO);

        eventPublisher.publishEvent(new ClienteAtendidoEvento(pedido.getCliente()));

        return repository.save(pedido);
    }

    public void alterarProfissionalPedido(PedidoAlteracaoInput pedidoAlteracaoInput, Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        if(pedido.getStatusPagamento().equals(StatusPagamento.PAGO)){
            throw new PedidoJaFoiPagoException("Não é permitido alterar profissional de pedido já recebido");
        }
        Profissional profissional = profissionalService.buscarPorId(pedidoAlteracaoInput.getProfissional().getId());
        pedido.setProfissional(profissional);
        pedido.setHorario(pedidoAlteracaoInput.getHorario());

    }

    public void confirmarPedido(Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        if(pedido.getStatusPedido().equals(StatusPedido.AGENDADO)){
            pedido.setStatusPedido(StatusPedido.CONFIRMADO);
        }else{
            throw new PedidoNaoPodeSerConfirmadoException(pedidoId);
        }
    }
}