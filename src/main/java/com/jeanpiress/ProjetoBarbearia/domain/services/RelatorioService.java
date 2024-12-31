package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.PedidoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.PedidoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.*;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.DataInicioFimMes;
import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbearia.domain.model.ComparacaoMes;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbearia.domain.model.Pedido;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ClienteRepository;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PedidoRepository;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ProfissionalRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Log4j2
@Service
public class RelatorioService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CaixaService caixaService;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private ClienteRepository clienteRepository;


    public RelatorioFaturamento buscarFaturamentoData(String dataInicio, String dataFim){
        LocalDateTime dataIncioDia = LocalDate.parse(dataInicio).atStartOfDay();
        LocalDateTime dataFimDia = LocalDate.parse(dataFim).atTime(LocalTime.MAX);
        List<Pedido> pedidos = pedidoRepository.findByDataPagamento(dataIncioDia, dataFimDia);
        return buscarFaturamento(pedidos);
    }

    public ComparacaoMes compararMetricasMesFechado(String dataInicio, String dataFim){

        DataInicioFimMes dataMes = gerarDatasParaComparar(LocalDate.parse(dataInicio), LocalDate.parse(dataFim));
        List<Pedido> pedidosPrimeiroMes = pedidoRepository.findByDataPagamento(dataMes.getInicioPrimeiroMes().atStartOfDay(),
                                                                               dataMes.getFimPrimeiroMes().atTime(LocalTime.MAX));
        List<Pedido> pedidosSegundoMes = pedidoRepository.findByDataPagamento(dataMes.getInicioSegundoMes().atStartOfDay(),
                                                                              dataMes.getFimSegundoMes().atTime(LocalTime.MAX));

        ComparacaoMes comparacaoMes = compararFaturamentos(pedidosPrimeiroMes, pedidosSegundoMes);
        comparacaoMes.setDataInicio(dataMes.getInicioPrimeiroMes());
        comparacaoMes.setDataFim(dataMes.getFimSegundoMes());

        return comparacaoMes;
    }

    public ComparacaoMes compararMetricasDataAtualMesmoPeriodoMesSelecionado(LocalDate dataFornecida){
        LocalDateTime dataAtual = LocalDateTime.now();
        List<Month> mesesComTrintaDias = Arrays.asList(Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER);
        List<Integer> maisDeVinteNoveDias = Arrays.asList(29, 30, 31);
        LocalDate dataPesquisa;

        boolean mesAtualComTrintaDias = mesesComTrintaDias.contains(dataFornecida.getMonth());
        boolean hojeDia31 = dataAtual.getDayOfMonth() == 31;
        boolean hojeDiaIgualSuperior29 = maisDeVinteNoveDias.contains(dataAtual.getDayOfMonth());
        boolean mesSelecionadoFeveiro = dataFornecida.getMonth().equals(Month.FEBRUARY);

        if(mesAtualComTrintaDias && hojeDia31 && !mesSelecionadoFeveiro){
            dataPesquisa = dataFornecida.withDayOfMonth(30);

        }else if(mesSelecionadoFeveiro && hojeDiaIgualSuperior29){
            dataPesquisa = dataFornecida.withDayOfMonth(28);

        }else {
            dataPesquisa = dataFornecida.withDayOfMonth(dataAtual.getDayOfMonth());
        }

        List<Pedido> pedidosMesAtual = pedidoRepository.findByDataPagamento(dataAtual.withDayOfMonth(1).toLocalDate().atStartOfDay(), dataAtual);
        List<Pedido> pedidosMesPesquisa = pedidoRepository.findByDataPagamento(dataPesquisa.withDayOfMonth(1).atStartOfDay(),
                                                                                dataPesquisa.atTime(dataAtual.getHour(), dataAtual.getMinute()));

        ComparacaoMes comparacaoMes = compararFaturamentos(pedidosMesPesquisa, pedidosMesAtual);
        comparacaoMes.setDataInicio(dataAtual.toLocalDate());
        comparacaoMes.setDataFim(dataPesquisa);

        return comparacaoMes;

    }

    public RelatorioComissaoDetalhada buscarComissaoPorProfissional(String dataInicio, String dataFim, Long profissionalId){
        LocalDateTime dataHoraInicio = LocalDate.parse(dataInicio).atStartOfDay();
        LocalDateTime dataHoraFim = LocalDate.parse(dataFim).atTime(23, 59, 59);
        List<Pedido> pedidos = pedidoRepository.findByDataPagamentoAndProfissionalId(dataHoraInicio, dataHoraFim, profissionalId);
        Profissional profissional = profissionalService.buscarPorId(profissionalId);
        RelatorioComissaoDetalhada relatorio = relatorioComissaoZerado(profissional);

        for (Pedido pedido : pedidos) {
             relatorio.setTotalComissao(relatorio.getTotalComissao().add(pedido.getComissaoGerada()));
             relatorio.setTotalpontos(relatorio.getTotalpontos().add(pedido.getPontuacaoProfissionalGerada()));
             relatorio.setTotalVendas(relatorio.getTotalVendas().add(pedido.getValorTotal()));
             relatorio.setClienteAtendidos(relatorio.getClienteAtendidos() + 1);

        }

        BigDecimal tkm = BigDecimal.ZERO;
        if(!relatorio.getTotalVendas().equals(BigDecimal.ZERO) || !BigDecimal.valueOf(relatorio.getClienteAtendidos()).equals(BigDecimal.ZERO)) {
            tkm = relatorio.getTotalVendas().divide(BigDecimal.valueOf(relatorio.getClienteAtendidos()));
        }
        relatorio.setTkm(tkm);
        relatorio.setPedidos(pedidos);

        return relatorio;
    }

    public List<RelatorioComissao> buscarTodasComissoes(String dataInicio, String dataFim){
        List<Pedido> pedidos = pedidoRepository.findByDataPagamento(LocalDate.parse(dataInicio).atStartOfDay(),
                                                                    LocalDate.parse(dataFim).atTime(LocalTime.MAX));
        List<Profissional> profissionais = profissionalRepository.buscarProfissionaisAtivos();
        List<RelatorioComissao> relatoriosComissoes = relatoriosComissoesZeradosComProfissionais(profissionais);

        for(Pedido pedido : pedidos){
            for(RelatorioComissao relatorioComissao: relatoriosComissoes) {
                if (pedido.getProfissional().getId().equals(relatorioComissao.getProfissional().getId())){
                    relatorioComissao.setTotalComissao(relatorioComissao.getTotalComissao().add(pedido.getComissaoGerada()));
                    relatorioComissao.setTotalpontos(relatorioComissao.getTotalpontos().add(pedido.getPontuacaoProfissionalGerada()));
                    relatorioComissao.setTotalVendas(relatorioComissao.getTotalVendas().add(pedido.getValorTotal()));
                    relatorioComissao.setClienteAtendidos(relatorioComissao.getClienteAtendidos() + 1);
                }
            }
        }
        for(RelatorioComissao relatorioComissao: relatoriosComissoes){
            if(!relatorioComissao.getTotalVendas().equals(BigDecimal.ZERO)) {
                BigDecimal tkm = relatorioComissao.getTotalVendas().
                        divide(BigDecimal.valueOf(relatorioComissao.getClienteAtendidos()));
                relatorioComissao.setTkm(tkm);
            }else relatorioComissao.setTkm(BigDecimal.ZERO);
        }

        return relatoriosComissoes;
    }

    public List<ClientesRetorno> buscarClientesParaRetornar(Integer quantidadeDias){
        OffsetDateTime dataAtual = OffsetDateTime.now();
        OffsetDateTime dataInicial = dataAtual.minusDays(quantidadeDias).withHour(0).withMinute(0).withSecond(0).withNano(0);
        OffsetDateTime dataFinal = dataAtual.plusDays(quantidadeDias).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        List<Cliente> clientes = clienteRepository.findByClientesRetornoEmDias(dataInicial, dataFinal);
        List<ClientesRetorno> clientesRetorno = new ArrayList<>();

        for(Cliente cliente: clientes){
            long diferencaDias = ChronoUnit.DAYS.between(dataAtual, cliente.getPrevisaoRetorno());

            ClientesRetorno clienteRetorno = ClientesRetorno.builder()
                    .cliente(cliente)
                    .diasPassadosRetorno(diferencaDias * -1)
                    .previsaoRetorno(cliente.getPrevisaoRetorno())
                    .build();

            clientesRetorno.add(clienteRetorno);
        }
        clientesRetorno.sort(Comparator.comparingLong(c -> c.getDiasPassadosRetorno()));

        return clientesRetorno;
    }

    public DataInicioFimMes gerarDatasParaComparar(LocalDate dataIncio, LocalDate dataFim) {
        LocalDate inicioPrimeiroMes = dataIncio.withDayOfMonth(1);

        LocalDate fimPrimeiroMes = inicioPrimeiroMes.plusMonths(1).minusDays(1);

        LocalDate inicioSegundoMes = dataFim.withDayOfMonth(1);

        LocalDate fimSegundoMes = inicioSegundoMes.plusMonths(1).minusDays(1);

        return DataInicioFimMes.builder()
                .inicioPrimeiroMes(inicioPrimeiroMes)
                .fimPrimeiroMes(fimPrimeiroMes)
                .inicioSegundoMes(inicioSegundoMes)
                .fimSegundoMes(fimSegundoMes)
                .build();

    }

    private List<RelatorioComissao> relatoriosComissoesZeradosComProfissionais(List<Profissional> profissionais){
        List<RelatorioComissao> relatoriosComissoes = new ArrayList<>();
         for(Profissional profissional : profissionais){
            RelatorioComissao relatorioComissao = RelatorioComissao.builder()
                    .profissional(profissional)
                    .totalComissao(BigDecimal.ZERO)
                    .totalpontos(BigDecimal.ZERO)
                    .totalVendas(BigDecimal.ZERO)
                    .clienteAtendidos(0)
                    .build();
            relatoriosComissoes.add(relatorioComissao);
        }
        return relatoriosComissoes;
    }


    private RelatorioComissaoDetalhada relatorioComissaoZerado(Profissional profissional){
        return  RelatorioComissaoDetalhada.builder()
                    .profissional(profissional)
                    .totalComissao(BigDecimal.ZERO)
                    .totalpontos(BigDecimal.ZERO)
                    .totalVendas(BigDecimal.ZERO)
                    .tkm(BigDecimal.ZERO)
                    .clienteAtendidos(0)
                .build();

        }

    private BigDecimal gerarTkmLoja(CaixaModel cd) {
        Integer quantidadePedidos = cd.getClientesAtendidos();
        BigDecimal faturamento = cd.getTotal();
        BigDecimal tkm = BigDecimal.ZERO;
        if(!faturamento.equals(BigDecimal.ZERO) && quantidadePedidos > 0){
            tkm = faturamento.divide(BigDecimal.valueOf(quantidadePedidos));
        }
        return tkm;
    }

    private ComparacaoMes compararFaturamentos(List<Pedido> pedidosPrimeiroMes, List<Pedido> pedidosSegundoMes){
        RelatorioFaturamento primeiroMes = buscarFaturamento(pedidosPrimeiroMes);
        RelatorioFaturamento segundoMes = buscarFaturamento(pedidosSegundoMes);

        BigDecimal diferencaFaturamento = segundoMes.getTotal().subtract(primeiroMes.getTotal());
        BigDecimal diferencaTkm = segundoMes.getTkmLoja().subtract(primeiroMes.getTkmLoja());
        int diferencaClientes = segundoMes.getClientesAtendidos() - primeiroMes.getClientesAtendidos();

        return ComparacaoMes.builder()
                .faturamentoPrimeiroMes(primeiroMes.getTotal())
                .faturamentoSegundoMes(segundoMes.getTotal())
                .diferencaFaturamento(diferencaFaturamento)
                .clientesAtendidosPrimeiroMes(primeiroMes.getClientesAtendidos())
                .clientesAtendidosSegundoMes(segundoMes.getClientesAtendidos())
                .diferencaClientesAtendidos(diferencaClientes)
                .tkmPrimeiroMes(primeiroMes.getTkmLoja())
                .tkmSegundoMes(segundoMes.getTkmLoja())
                .diferencaTkm(diferencaTkm)
                .build();
    }

    private RelatorioFaturamento buscarFaturamento(List<Pedido> pedidos){
        CaixaModel caixa = caixaService.gerarCaixa(pedidos);
        return RelatorioFaturamento.builder()
                .dinheiro(caixa.getDinheiro())
                .pix(caixa.getPix())
                .debito(caixa.getDebito())
                .credito(caixa.getCredito())
                .voucher(caixa.getVoucher())
                .pontos(caixa.getPontos())
                .total(caixa.getTotal())
                .tkmLoja(gerarTkmLoja(caixa))
                .clientesAtendidos(caixa.getClientesAtendidos())
                .quantidadeProdutosVendidos(caixa.getQuantidadeProdutosVendidos())
                .faturamentos(caixa.getFaturamentos())
                .build();
    }


}
