package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.ItemPedidoInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ItemPedidoInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.PedidoAlteracaoInput;
import com.jeanpiress.ProjetoBarbearia.core.security.CsbSecurity;
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

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CsbSecurity security;

    @Autowired
    private ItemPedidoInputDissembler itemPedidoInputDissembler;


    public Pedido buscarPorId(Long pedidoId){
        return repository.findById(pedidoId).
                orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
    }

    public Pedido adicionar(Pedido pedido){
        return repository.save(pedido);
    }

    public Pedido criar(Pedido pedido, String statusPedido) {
        if(Objects.nonNull(pedido.getHorario()) && pedido.getHorario().isBefore(OffsetDateTime.now())){
            throw new HorarioInvalidoException("Não é possivel marcar um horario em uma data que já passou");
        }
        if(Objects.isNull(pedido.getHorario())){
            pedido.setHorario(OffsetDateTime.now());
        }
        if(Objects.nonNull(pedido.getDuracao())) {
            pedido.setFimHorario(adicionarFimHorario(pedido.getHorario(), pedido.getDuracao()));
        }

        StatusPedido statusPedidoFinal;
        try{
            statusPedidoFinal = StatusPedido.valueOf(statusPedido.toUpperCase());
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Falha ao converter status");
        }

        Cliente cliente = clienteService.buscarPorId(pedido.getCliente().getId());
        Profissional profissional = profissionalService.buscarPorId(pedido.getProfissional().getId());
        pedido.setCliente(cliente);
        pedido.setProfissional(profissional);
        List<ItemPedido> itensPedidos = new ArrayList<>();
        pedido.setItemPedidos(itensPedidos);
        preencherPedido(pedido);
        pedido.setCriadoAs(OffsetDateTime.now());
        Usuario usuario = usuarioService.buscarPorId(security.getUsuarioId());
        pedido.setCriadoPor(usuario);
        pedido.setStatusPedido(statusPedidoFinal);
        

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

    public Pedido adicionarItemPedidoPorLista(Long pedidoId, List<ItemPedidoInput> itensPedidosInput){
        Pedido pedido = buscarPorId(pedidoId);
        Long profissionalId = pedido.getProfissional().getId();
        List<ItemPedido> itensPedidosSalvos = new ArrayList<>();
        for(ItemPedidoInput itemPedidoInput : itensPedidosInput){
            ItemPedido itemPedido = itemPedidoInputDissembler.toDomainObject(itemPedidoInput);
            itensPedidosSalvos.add(itemPedidoService.adicionar(itemPedido));
        }

        for(ItemPedido itemPedidoSalvo : itensPedidosSalvos) {

            if (pedido.getItemPedidos().contains(itemPedidoSalvo)) {
                throw new ItemPedidoJaAdicionadoException(itemPedidoSalvo.getId());
            }

            pedido.getItemPedidos().add(itemPedidoSalvo);
            BigDecimal comissaoItemPedido = comissaoPorItem(profissionalId, itemPedidoSalvo);
            BigDecimal comissaoPedido = pedido.getComissaoGerada().add(comissaoItemPedido);
            pedido.setComissaoGerada(comissaoPedido);
            BigDecimal valorTotal = pedido.getValorTotal().add(itemPedidoSalvo.getPrecoTotal());
            pedido.setValorTotal(valorTotal);
            BigDecimal pontuacaoProfissional = pedido.getPontuacaoProfissionalGerada().add(gerarPontuacaoProfissional(itemPedidoSalvo));
            pedido.setPontuacaoProfissionalGerada(pontuacaoProfissional);
            BigDecimal pontuacaoCliente = pedido.getPontuacaoClienteGerada().add(gerarPontuacaoCliente(itemPedidoSalvo));
            pedido.setPontuacaoClienteGerada(pontuacaoCliente);
        }
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

    public Pedido removerTodosItensPedido(Pedido pedido){
        pedido.setItemPedidos(new ArrayList<>());
        pedido.setComissaoGerada(BigDecimal.ZERO);
        pedido.setValorTotal(BigDecimal.ZERO);
        pedido.setPontuacaoProfissionalGerada(BigDecimal.ZERO);
        pedido.setPontuacaoClienteGerada(BigDecimal.ZERO);
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
        Usuario usuario = usuarioService.buscarPorId(security.getUsuarioId());
        pedido.setRecibidoPor(usuario);

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

        pedido.setStatusPedido(StatusPedido.FINALIZADO);

        pedido = repository.save(pedido);

        log.info(pedido.getCliente().getNome());

        Usuario usuario = usuarioService.buscarPorId(security.getUsuarioId());
        pedido.setRecibidoPor(usuario);

        eventPublisher.publishEvent(new ClienteAtendidoEvento(pedido.getCliente()));

        return pedido;
    }

    public Pedido alterarInfoPedido(PedidoAlteracaoInput pedidoAlteracaoInput, Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        if(pedido.getStatusPagamento().equals(StatusPagamento.PAGO)){
            throw new PedidoJaFoiPagoException("Não é permitido alterar profissional de pedido já recebido");
        }
        if(pedidoAlteracaoInput.getHorario().isBefore(OffsetDateTime.now().minusMinutes(1))){
            throw new HorarioInvalidoException("Não é possivel marcar um horario em uma data que já passou");
        }
        Profissional profissional = profissionalService.buscarPorId(pedidoAlteracaoInput.getProfissional().getId());
        pedido.setProfissional(profissional);
        pedido.setDescricao(pedidoAlteracaoInput.getDescricao());
        pedido.setHorario(pedidoAlteracaoInput.getHorario());
        Usuario usuario = usuarioService.buscarPorId(security.getUsuarioId());
        pedido.setAlteradoPor(usuario);
        pedido.setModificadoAs(OffsetDateTime.now());
        pedido.setDuracao(pedidoAlteracaoInput.getDuracao());
        pedido.setFimHorario(adicionarFimHorario(pedidoAlteracaoInput.getHorario(), pedidoAlteracaoInput.getDuracao()));

        return repository.save(pedido);
    }

    public Pedido alterarProfissional(Long profissionalId, Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        if(pedido.getStatusPagamento().equals(StatusPagamento.PAGO)){
            throw new PedidoJaFoiPagoException("Não é permitido alterar profissional de pedido já recebido");
        }
        Profissional profissional = profissionalService.buscarPorId(profissionalId);
        pedido.setProfissional(profissional);
        Usuario usuario = usuarioService.buscarPorId(security.getUsuarioId());
        pedido.setAlteradoPor(usuario);
        pedido.setModificadoAs(OffsetDateTime.now());

        return pedido;
    }

    public void confirmarPedido(Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        if(pedido.getStatusPedido().equals(StatusPedido.AGENDADO)){
            pedido.setStatusPedido(StatusPedido.CONFIRMADO);
            repository.save(pedido);
        }else{
            throw new PedidoNaoPodeSerConfirmadoException(pedidoId);
        }

    }

    public void iniciarExecucaoPedido(Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        if(pedido.getStatusPedido().equals(StatusPedido.CANCELADO) ||
                pedido.getStatusPedido().equals(StatusPedido.FINALIZADO) ||
                pedido.getStatusPedido().equals(StatusPedido.EXCLUIDO)){

            throw new PedidoNaoPodeSerConfirmadoException(pedidoId);
        }else{
            pedido.setStatusPedido(StatusPedido.EMATENDIMENTO);
            pedido.setInicioAtendimento(OffsetDateTime.now());
            repository.save(pedido);
        }
    }

    public void cancelarPedido(Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        Usuario usuario = usuarioService.buscarPorId(security.getUsuarioId());
        if(pedido.getStatusPagamento().equals(StatusPagamento.PAGO)){
            throw new PedidoNaoPodeSerCanceladoException("Pedidos pagos não podem ser cancelados");
        }
        pedido.setStatusPedido(StatusPedido.CANCELADO);
        pedido.setStatusPagamento(StatusPagamento.AGUARDANDO_PAGAMENTO);
        pedido.setCanceladoAs(OffsetDateTime.now());
        pedido.setCanceladoPor(usuario);
        repository.save(pedido);
    }

    public void excluirPedido(Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        Usuario usuario = usuarioService.buscarPorId(security.getUsuarioId());
        boolean isGerente = usuario.getPermissoes().stream().anyMatch(permissao -> permissao.getId().equals(1L));
        if(pedido.getStatusPagamento().equals(StatusPagamento.PAGO) && !isGerente){
            throw new PedidoNaoPodeSerCanceladoException("Apenas os gerentes do sistema pode cancelar pedidos pagos");
        }
        pedido.setStatusPedido(StatusPedido.EXCLUIDO);
        pedido.setStatusPagamento(StatusPagamento.CANCELADO);
        pedido.setExcluidoAs(OffsetDateTime.now());
        pedido.setExcluidoPor(usuario);
        repository.save(pedido);
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

   private OffsetDateTime adicionarFimHorario(OffsetDateTime dataHorario, String duracao){
       String[] parts = duracao.split(":");
       int horas = Integer.parseInt(parts[0]);
       int minutos = Integer.parseInt(parts[1]);

       return dataHorario.plusHours(horas).plusMinutes(minutos);
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
