package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao.FormaPagamentoJson;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbaria.domain.Enuns.StatusPedido;
import com.jeanpiress.ProjetoBarbaria.domain.eventos.ClienteAtendidoEvento;
import com.jeanpiress.ProjetoBarbaria.domain.eventos.ProdutoCriadoEvento;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.PedidoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.*;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

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



    public Pedido buscarPorId(Long pedidoId){
        return repository.findById(pedidoId).
                orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
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
            itemPedido.setPedido(pedido);
            itemPedidoService.adicionar(itemPedido);
            pedido.adicionarItemPedido(itemPedido);
            BigDecimal comissaoGerada = comissaoPorItem(profissionalId, itemPedido);
            BigDecimal comissaoPedido = pedido.getComissaoGerada().add(comissaoGerada);
            pedido.setComissaoGerada(comissaoPedido);
            BigDecimal valorTotal = pedido.getValorTotal().add(itemPedido.getPrecoTotal());
            pedido.setValorTotal(valorTotal);
            BigDecimal pontuacao = pedido.getPontuacaoGerada().add(gerarPontuacao(itemPedido));
            pedido.setPontuacaoGerada(pontuacao);
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

}
