package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.ClienteAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.PedidoAssembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.*;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ClienteResumo;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalIdNome;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.DataInicioFim;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.DataInicioFimMes;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.DataJson;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.MesAnoJson;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.FormatoDataException;
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
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private ClienteAssembler clienteAssembler;

    @Autowired
    private PedidoAssembler pedidoAssembler;



    public RelatorioFaturamento buscarFaturamentoDataJson(DataJson dataJson){
        List<Pedido> pedidos = buscarPedidosPorDataJson(dataJson);
        return buscarFaturamento(pedidos);
    }


    public RelatorioComissaoDetalhada buscarComissaoPorProfissional(DataJson dataInicioFim, Long profissionalId){
        List<Pedido> pedidos = buscarPedidosPorDataEProfissional(dataInicioFim, profissionalId);
        Profissional profissional = profissionalService.buscarPorId(profissionalId);
        RelatorioComissaoDetalhada relatorio = relatorioComissaoZerado(profissional);

        for (Pedido pedido : pedidos) {
             relatorio.setTotalComissao(relatorio.getTotalComissao().add(pedido.getComissaoGerada()));
             relatorio.setTotalpontos(relatorio.getTotalpontos().add(pedido.getPontuacaoGerada()));
             relatorio.setTotalVendas(relatorio.getTotalVendas().add(pedido.getValorTotal()));
             relatorio.setClienteAtendidos(relatorio.getClienteAtendidos() + 1);

        }

        BigDecimal tkm = BigDecimal.ZERO;
        if(!relatorio.getTotalVendas().equals(BigDecimal.ZERO) || !BigDecimal.valueOf(relatorio.getClienteAtendidos()).equals(BigDecimal.ZERO)) {
            tkm = relatorio.getTotalVendas().divide(BigDecimal.valueOf(relatorio.getClienteAtendidos()));
        }
        relatorio.setTkm(tkm);
        relatorio.setPedidos(pedidoAssembler.collectionToModelResumo(pedidos));

        return relatorio;
    }



    public List<RelatorioComissao> buscarTodasComissoes(DataJson dataInicioFim){
        List<Pedido> pedidos = buscarPedidosPorDataJson(dataInicioFim);
        Set<Profissional> profissionais = profissionalRepository.buscarProfissionaisAtivos();
        List<RelatorioComissao> relatoriosComissoes = relatoriosComissoesZeradosComProfissionais(profissionais);

        for(Pedido pedido : pedidos){
            for(RelatorioComissao relatorioComissao: relatoriosComissoes) {
                if (pedido.getProfissional().getId().equals(relatorioComissao.getProfissional().getId())){
                    relatorioComissao.setTotalComissao(relatorioComissao.getTotalComissao().add(pedido.getComissaoGerada()));
                    relatorioComissao.setTotalpontos(relatorioComissao.getTotalpontos().add(pedido.getPontuacaoGerada()));
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

    public List<ClientesRetorno> buscarClientesParaRetornarHoje(Integer quantidadeDias){
        OffsetDateTime dataAtual = OffsetDateTime.now();
        OffsetDateTime dataInicial = dataAtual.minusDays(quantidadeDias).withHour(0).withMinute(0).withSecond(0).withNano(0);
        OffsetDateTime dataFinal = dataAtual.plusDays(quantidadeDias).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        List<Cliente> clientes = clienteRepository.findByClientesRetornoEmDias(dataInicial, dataFinal);
        List<ClientesRetorno> clientesRetorno = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for(Cliente cliente: clientes){
            ClienteResumo clienteResumo = clienteAssembler.toClienteResumo(cliente);
            long diferencaDias = ChronoUnit.DAYS.between(dataAtual, cliente.getPrevisaoRetorno());
            if(dataAtual.isAfter(cliente.getPrevisaoRetorno())){ diferencaDias -= 1;}
            String dataFormatada = formatter.format(cliente.getPrevisaoRetorno());

            ClientesRetorno cr = ClientesRetorno.builder()
                    .cliente(clienteResumo)
                    .diasPassados(diferencaDias)
                    .previsaoRetorno(dataFormatada)
                    .build();

            clientesRetorno.add(cr);
        }
        clientesRetorno.sort(Comparator.comparingLong(c -> c.getDiasPassados()));

        return clientesRetorno;
    }

    public ComparacaoMes compararMes(DataJson dataJson){
        DataInicioFimMes dataMes = gerarDatasParaComparar(dataJson);
        List<Pedido> pedidosPrimeiroMes = pedidoRepository.findByDataPagamento(dataMes.getInicioPrimeiroMes(), dataMes.getFimPrimeiroMes());
        List<Pedido> pedidosSegundoMes = pedidoRepository.findByDataPagamento(dataMes.getInicioSegundoMes(), dataMes.getFimSegundoMes());

        ComparacaoMes comparacaoMes = compararFaturamentos(pedidosPrimeiroMes, pedidosSegundoMes);
        comparacaoMes.setDataInicio(dataMes.getInicioPrimeiroMes());
        comparacaoMes.setDataFim(dataMes.getFimSegundoMes());

        return comparacaoMes;
    }

    public ComparacaoMes compararPorPeriodoMes(MesAnoJson mesAnoJson){
        OffsetDateTime dataAtual = OffsetDateTime.now();
        OffsetDateTime dataFornecida = converterMesAnoJson(mesAnoJson);
        List<Month> mesesComTrintaDias = Arrays.asList(Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER);
        List<Integer> maisDeVinteNoveDias = Arrays.asList(29, 30, 31);
        OffsetDateTime dataPesquisa;

        if(mesesComTrintaDias.contains(dataFornecida.getMonth()) && dataAtual.getDayOfMonth() == 31
                && !dataFornecida.getMonth().equals(Month.FEBRUARY)){
            dataPesquisa = dataFornecida.withDayOfMonth(30);
        }else if(dataFornecida.getMonth().equals(Month.FEBRUARY) && maisDeVinteNoveDias.contains(dataAtual.getDayOfMonth())){
            dataPesquisa = dataFornecida.withDayOfMonth(28);
        }else {
            dataPesquisa = dataFornecida.withDayOfMonth(dataAtual.getDayOfMonth());
        }

        List<Pedido> pedidosMesAtual = pedidoRepository.findByDataPagamento(dataAtual.withDayOfMonth(1), dataAtual);
        List<Pedido> pedidosMesPesquisa = pedidoRepository.findByDataPagamento(dataPesquisa.withDayOfMonth(1), dataPesquisa);

        ComparacaoMes comparacaoMes = compararFaturamentos(pedidosMesPesquisa, pedidosMesAtual);
        comparacaoMes.setDataInicio(dataAtual);
        comparacaoMes.setDataFim(dataPesquisa);

        return comparacaoMes;

    }


    private DataInicioFimMes gerarDatasParaComparar(DataJson dataJson) {
        DataInicioFim inicioFim = converterDataJson(dataJson);

        OffsetDateTime inicioPrimeiroMes = inicioFim.getInicio().withDayOfMonth(1);

        OffsetDateTime fimPrimeiroMes = inicioPrimeiroMes.plusMonths(1).minusDays(1)
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        OffsetDateTime inicioSegundoMes = inicioFim.getFim().withDayOfMonth(1);

        OffsetDateTime fimSegundoMes = inicioSegundoMes.plusMonths(1).minusDays(1)
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);



        return DataInicioFimMes.builder()
                .inicioPrimeiroMes(inicioPrimeiroMes)
                .fimPrimeiroMes(fimPrimeiroMes)
                .inicioSegundoMes(inicioSegundoMes)
                .fimSegundoMes(fimSegundoMes)
                .build();

    }


    private List<RelatorioComissao> relatoriosComissoesZeradosComProfissionais(Set<Profissional> profissionais){
        List<RelatorioComissao> relatoriosComissoes = new ArrayList<>();
         for(Profissional profissional : profissionais){
            RelatorioComissao relatorioComissao = RelatorioComissao.builder()
                    .profissional(converterProfissional(profissional))
                    .totalComissao(BigDecimal.ZERO)
                    .totalpontos(BigDecimal.ZERO)
                    .totalVendas(BigDecimal.ZERO)
                    .clienteAtendidos(0)
                    .build();
            relatoriosComissoes.add(relatorioComissao);
        }
        return relatoriosComissoes;
    }

    private ProfissionalIdNome converterProfissional(Profissional profissional) {
        return ProfissionalIdNome.builder()
                .id(profissional.getId())
                .nome(profissional.getNome())
                .build();
    }

    private RelatorioComissaoDetalhada relatorioComissaoZerado(Profissional profissional){
        return  RelatorioComissaoDetalhada.builder()
                    .profissional(converterProfissional(profissional))
                    .totalComissao(BigDecimal.ZERO)
                    .totalpontos(BigDecimal.ZERO)
                    .totalVendas(BigDecimal.ZERO)
                    .clienteAtendidos(0)
                .build();

        }


    private List<Pedido> buscarPedidosPorDataJson(DataJson dataInicioFim){
        DataInicioFim InicioFim = converterDataJson(dataInicioFim);
        OffsetDateTime inicio = InicioFim.getInicio();
        OffsetDateTime fim = InicioFim.getFim();
        return pedidoRepository.findByDataPagamento(inicio, fim);
    }



    private List<Pedido> buscarPedidosPorDataEProfissional(DataJson dataInicioFim, Long profissionalId){
        DataInicioFim InicioFim = converterDataJson(dataInicioFim);
        OffsetDateTime inicio = InicioFim.getInicio();
        OffsetDateTime fim = InicioFim.getFim();
        return pedidoRepository.findByDataPagamentoAndProfissionalId(inicio, fim, profissionalId);
    }

    private BigDecimal gerarTkmLoja(List<Pedido> pedidos, CaixaModel cd) {
        Integer quantidadePedidos = pedidos.size();
        BigDecimal faturamento = cd.getTotal();
        BigDecimal tkm = BigDecimal.ZERO;
        if(!faturamento.equals(BigDecimal.ZERO) && quantidadePedidos > 0){
            tkm = faturamento.divide(BigDecimal.valueOf(quantidadePedidos));
        }

        return tkm;
    }

    private DataInicioFim converterDataJson(DataJson dataInicioFim) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDateTime inicio = LocalDate.parse(dataInicioFim.getInicio(), formatter).atStartOfDay();
            LocalDateTime fim = LocalDate.parse(dataInicioFim.getFim(), formatter).atTime(23, 59, 59, 999999999);

            OffsetDateTime dataInicio = inicio.atOffset(ZoneOffset.UTC);
            OffsetDateTime dataFim = fim.atOffset(ZoneOffset.UTC);

            return DataInicioFim.builder()
                    .inicio(dataInicio)
                    .fim(dataFim)
                    .build();
        }catch (DateTimeException e){
            throw new FormatoDataException("Formato de data incorreto, use: aaaa-mm-dd");
        }
    }

    private OffsetDateTime converterMesAnoJson (MesAnoJson mesAnoJson){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            OffsetDateTime horaAtua = OffsetDateTime.now();

            LocalDateTime data = LocalDate.parse(mesAnoJson.getMesAno(), formatter).atTime(
                    horaAtua.getHour(), horaAtua.getMinute(), horaAtua.getSecond()
            );

            return data.atOffset(ZoneOffset.UTC);

        }catch (DateTimeException e){
            throw new FormatoDataException("Formato de data incorreto, use: aaaa-mm-dd");
        }
    }

    private OffsetDateTime definirfimDia(OffsetDateTime data){
        return data.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
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
        CaixaModel cm = caixaService.gerarCaixa(pedidos);

        return RelatorioFaturamento.builder()
                .dinheiro(cm.getDinheiro())
                .pix(cm.getPix())
                .debito(cm.getDebito())
                .credito(cm.getCredito())
                .voucher(cm.getVoucher())
                .pontos(cm.getPontos())
                .total(cm.getTotal())
                .tkmLoja(gerarTkmLoja(pedidos, cm))
                .clientesAtendidos(cm.getClientesAtendidos())
                .quantidadeProdutosVendidos(cm.getQuantidadeProdutosVendidos())
                .build();
    }


}
