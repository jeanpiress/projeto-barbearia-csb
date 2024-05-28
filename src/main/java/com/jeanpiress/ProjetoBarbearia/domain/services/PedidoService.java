package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.PedidoAlteracaoInput;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.FormaPagamentoJson;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPedido;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.RealizacaoItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.eventos.ClienteAtendidoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.eventos.PacoteRealizadoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.*;
import com.jeanpiress.ProjetoBarbearia.domain.model.*;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PedidoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Log4j2
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
    private PacoteService pacoteService;

    @Autowired
    private ItemPacoteService itemPacoteService;


    public Pedido buscarPorId(Long pedidoId){
        return repository.findById(pedidoId).
                orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
    }

    public Pedido adicionar(Pedido pedido){
        return repository.save(pedido);
    }

    public Pedido criar(Pedido pedido) {
        Cliente cliente = clienteService.buscarPorId(pedido.getCliente().getId());
        Profissional profissional = profissionalService.buscarPorId(pedido.getProfissional().getId());
        pedido.setCliente(cliente);
        pedido.setProfissional(profissional);
        List<ItemPedido> itensPedidos = new ArrayList<>();
        pedido.setItemPedidos(itensPedidos);
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

        if(pedido.getItemPedidos().contains(itemPedido)) {
            throw new ItemPedidoJaAdicionadoException(itemPedido.getId());
        }

        pedido.getItemPedidos().add(itemPedido);
        BigDecimal comissaoItemPedido = comissaoPorItem(profissionalId, itemPedido);
        BigDecimal comissaoPedido = pedido.getComissaoGerada().add(comissaoItemPedido);
        pedido.setComissaoGerada(comissaoPedido);
        BigDecimal valorTotal = pedido.getValorTotal().add(itemPedido.getPrecoTotal());
        pedido.setValorTotal(valorTotal);
        BigDecimal pontuacaoProfissional = pedido.getPontuacaoProfissionalGerada().add(gerarPontuacaoProfissional(itemPedido));
        pedido.setPontuacaoProfissionalGerada(pontuacaoProfissional);
        BigDecimal pontuacaoCliente = pedido.getPontuacaoClienteGerada().add(gerarPontuacaoCliente(itemPedido));
        pedido.setPontuacaoClienteGerada(pontuacaoCliente);
        return repository.save(pedido);
    }


    public Pedido removerItemPedido(Long pedidoId, Long itemPedidoId){
        Pedido pedido = buscarPorId(pedidoId);
        ItemPedido itemPedido = itemPedidoService.buscarPorId(itemPedidoId);
        Long profissionalId = pedido.getProfissional().getId();
        if(!pedido.getItemPedidos().contains(itemPedido)) {
            throw new PedidoNaoContemItemPedidoException(pedido.getId(), itemPedido.getId());
        }
        pedido.removerItemPedido(itemPedido);
        itemPedidoService.remover(itemPedidoId);
        BigDecimal comissaoGerada = comissaoPorItem(profissionalId, itemPedido);
        BigDecimal comissaoPedido = pedido.getComissaoGerada().subtract(comissaoGerada);
        pedido.setComissaoGerada(comissaoPedido);
        BigDecimal valorTotal = pedido.getValorTotal().subtract(itemPedido.getPrecoTotal());
        pedido.setValorTotal(valorTotal);
        BigDecimal pontuacaoProfissional = pedido.getPontuacaoProfissionalGerada().subtract(gerarPontuacaoProfissional(itemPedido));
        pedido.setPontuacaoProfissionalGerada(pontuacaoProfissional);
        BigDecimal pontuacaoCliente = pedido.getPontuacaoClienteGerada().subtract(gerarPontuacaoCliente(itemPedido));
        pedido.setPontuacaoClienteGerada(pontuacaoCliente);

        return repository.save(pedido);
    }

    public Pedido realizarPagamentoComPedidoExistente(RealizacaoItemPacote realizacaoItemPacote, Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        if(pedido.getStatusPagamento() == StatusPagamento.PAGO){
            throw new PedidoJaFoiPagoException(pedidoId);
        }

        Pacote pacote = pacoteService.buscarPorId(realizacaoItemPacote.getPacote().getId());

        if(pacote.getDataVencimento().isBefore(OffsetDateTime.now())){
            throw new PacoteVencidoException(pacote.getId());
        }

        Cliente clientePedido = pedido.getCliente();
        Cliente clientePacote = pacote.getCliente();

        if(!clientePacote.equals(clientePedido)){
            throw new ClientesDiferentesException("Cliente do pedido não é o titular do pacote");
        }

        Long itemPacoteId = realizacaoItemPacote.getItemPacote().getId();
        ItemPacote itemPacote = itemPacoteService.buscarPorId(itemPacoteId);

        if(!pacote.getItensAtivos().contains(itemPacote)){
            throw new ItemPacoteNaoEncontradoEmItemAtivoException(itemPacoteId);
        }

        pedido.setPontuacaoProfissionalGerada(gerarPontuacaoProfissional(itemPacote.getItemPedido()));
        pedido.setStatusPedido(StatusPedido.FINALIZADO);
        pedido.setFormaPagamento(FormaPagamento.VOUCHER);
        pedido.setStatusPagamento(StatusPagamento.PAGO);
        pedido.setDataPagamento(OffsetDateTime.now());
        pedido.setValorTotal(itemPacote.getItemPedido().getPrecoTotal());
        pacoteService.alterarAtivoParaConsumido(pacote, pedido.getProfissional(), itemPacoteId);

        repository.save(pedido);
        pacoteService.adicionar(pacote);

        eventPublisher.publishEvent(new ClienteAtendidoEvento(pedido.getCliente()));

        return pedido;
    }



    public Pedido realizarPagamento(FormaPagamentoJson formaPagamento, Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        String letrasMaiusculas = formaPagamento.getFormaPagamento().toUpperCase();
        formaPagamento.setFormaPagamento(letrasMaiusculas);


        if(formaPagamento.getFormaPagamento().equals("DINHEIRO")){
            pedido.setFormaPagamento(FormaPagamento.DINHEIRO);
        }
        else if(formaPagamento.getFormaPagamento().equals("PIX")){
            pedido.setFormaPagamento(FormaPagamento.PIX);
        }
        else if(formaPagamento.getFormaPagamento().equals("DEBITO")){
            pedido.setFormaPagamento(FormaPagamento.DEBITO);
        }
        else if(formaPagamento.getFormaPagamento().equals("CREDITO")){
            pedido.setFormaPagamento(FormaPagamento.CREDITO);
        }
        else if(formaPagamento.getFormaPagamento().equals("VOUCHER")){
            throw new OperacaoNaoRealizadaException();
        }
        else if(formaPagamento.getFormaPagamento().equals("PONTO")){
            throw new OperacaoNaoRealizadaException();
        }
        else{
            throw new FormaPagamentoNaoReconhecidaException();
        }
        pedido.setDataPagamento(OffsetDateTime.now());

        pedido.setStatusPagamento(StatusPagamento.PAGO);

        pedido = repository.save(pedido);

        log.info(pedido.getCliente().getNome());

        eventPublisher.publishEvent(new ClienteAtendidoEvento(pedido.getCliente()));

        return pedido;
    }

    public void alterarProfissionalOuHorarioPedido(PedidoAlteracaoInput pedidoAlteracaoInput, Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        if(pedido.getStatusPagamento().equals(StatusPagamento.PAGO)){
            throw new PedidoJaFoiPagoException("Não é permitido alterar profissional de pedido já recebido");
        }
        if(pedidoAlteracaoInput.getHorario().isBefore(OffsetDateTime.now())){
            throw new HorarioInvalidoException("Não é possivel marcar um horario em uma data que já passou");
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

    private BigDecimal comissaoPorItem(Long profissionalId, ItemPedido itemPedido){
        Long produtoId = itemPedido.getProduto().getId();
        Comissao comissao = comissaoService.buscarPorProfissionalProduto(profissionalId, produtoId);
        BigDecimal comissaoProdutoUnitario = comissaoService.calculoComissaoProduto(comissao);
        Integer quantidade = itemPedido.getQuantidade();
        BigDecimal comissaoProdutoFinal = comissaoProdutoUnitario.multiply(new BigDecimal(quantidade));

        return  comissaoProdutoFinal;
    }

    private BigDecimal gerarPontuacaoProfissional(ItemPedido itemPedido){
        BigDecimal pesoPontuacao = itemPedido.getProduto().getPesoPontuacaoProfissional();
        return gerarPontuacao(pesoPontuacao, itemPedido);
    }

    private BigDecimal gerarPontuacaoCliente(ItemPedido itemPedido){
        BigDecimal pesoPontuacao = itemPedido.getProduto().getPesoPontuacaoCliente();
        return gerarPontuacao(pesoPontuacao, itemPedido);
    }

    private BigDecimal gerarPontuacao(BigDecimal pesoPontuacao, ItemPedido itemPedido){
        BigDecimal valorProduto = itemPedido.getProduto().getPreco();
        BigDecimal pontuacaProduto = valorProduto.multiply(pesoPontuacao);
        BigDecimal pontuacaoItem = pontuacaProduto.multiply(BigDecimal.valueOf(itemPedido.getQuantidade()));

        return  pontuacaoItem;
    }


    private void preencherPedido(Pedido pedido){
        pedido.setStatusPedido(StatusPedido.AGENDADO);
        pedido.setFormaPagamento(FormaPagamento.AGUARDANDO_PAGAMENTO);
        pedido.setStatusPagamento(StatusPagamento.AGUARDANDO_PAGAMENTO);
        pedido.setComissaoGerada(BigDecimal.ZERO);
        pedido.setValorTotal(BigDecimal.ZERO);
        pedido.setPontuacaoProfissionalGerada(BigDecimal.ZERO);
        pedido.setPontuacaoClienteGerada(BigDecimal.ZERO);

   }

    @EventListener
    public void criarPedidoPorPacote(PacoteRealizadoEvento pacoteRealizado) {
        Pacote pacote = pacoteRealizado.getPacote();
        Cliente cliente = pacote.getCliente();
        List<ItemPacote> itensConsumidos = pacote.getItensConsumidos();
        ItemPacote ultimoItemConsumido = itensConsumidos.stream().max(Comparator.comparing(ItemPacote::getDataConsumo)).get();
        Profissional profissional = ultimoItemConsumido.getProfissional();
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setProfissional(profissional);
        pedido.setPontuacaoProfissionalGerada(gerarPontuacaoProfissional(ultimoItemConsumido.getItemPedido()));
        List<ItemPedido> itensPedidos = pedido.getItemPedidos();
        ItemPedido itemPedido = ultimoItemConsumido.getItemPedido();
        itensPedidos.add(itemPedido);
        pedido.setItemPedidos(itensPedidos);
        pedido.setComissaoGerada(comissaoPorItem(pedido.getProfissional().getId(), itemPedido));
        pedido.setValorTotal(itemPedido.getPrecoTotal());
        repository.save(pedido);

        pacoteService.adicionar(pacote);
    }
}
