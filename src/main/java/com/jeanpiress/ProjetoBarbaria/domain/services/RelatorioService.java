package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.api.converteDto.assebler.ClienteAssembler;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ClienteResumo;
import com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao.DataPagamentoInicioFim;
import com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao.DataPagamentoJson;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.FormatoDataException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.CaixaModel;
import com.jeanpiress.ProjetoBarbaria.domain.model.Pedido;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.ClientesRetorno;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.RelatorioComissao;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.RelatorioFaturamento;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.ClienteRepository;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.PedidoRepository;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
public class RelatorioService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CaixaService caixaService;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteAssembler clienteAssembler;




    public RelatorioFaturamento buscarFaturamento(DataPagamentoJson dataInicioFim){
       List<Pedido> pedidos = buscarPedidosPorData(dataInicioFim);
       CaixaModel cm = caixaService.gerarCaixa(pedidos);

        return RelatorioFaturamento.builder()
                .dinheiro(cm.getDinheiro())
                .pix(cm.getPix())
                .debito(cm.getDebito())
                .credito(cm.getCredito())
                .tkmLoja(gerarTkmLoja(pedidos, cm))
                .clientesAtendidos(cm.getClientesAtendidos())
                .quantidadeProdutosVendidos(cm.getQuantidadeProdutosVendidos())
                .build();
    }


    public List<RelatorioComissao> buscarComissoes(DataPagamentoJson dataInicioFim){
        List<Pedido> pedidos = buscarPedidosPorData(dataInicioFim);
        Set<Profissional> profissionais = profissionalRepository.buscarProfissionaisAtivos();
        List<RelatorioComissao> relatoriosComissoes = relatoriosComissoesZeradosComProfissionais(profissionais);

        for(Pedido pedido : pedidos){
            for(RelatorioComissao relatorioComissao: relatoriosComissoes) {
                if (pedido.getProfissional().equals(relatorioComissao.getProfissional())){
                    relatorioComissao.setTotalComissao(relatorioComissao.getTotalComissao().add(pedido.getComissaoGerada()));
                    relatorioComissao.setTotalpontos(relatorioComissao.getTotalpontos().add(pedido.getPontuacaoGerada()));
                    relatorioComissao.setTotalVendas(relatorioComissao.getTotalVendas().add(pedido.getValorTotal()));
                    relatorioComissao.setClienteAtendidos(relatorioComissao.getClienteAtendidos() + 1);
                }
            }
        }
        for(RelatorioComissao relatorioComissao: relatoriosComissoes){
            BigDecimal tkm = relatorioComissao.getTotalVendas()
                    .divide(BigDecimal.valueOf(relatorioComissao.getClienteAtendidos()));
            relatorioComissao.setTkm(tkm);
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

    private List<RelatorioComissao> relatoriosComissoesZeradosComProfissionais(Set<Profissional> profissionais){
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

    private List<Pedido> buscarPedidosPorData(DataPagamentoJson dataInicioFim){
        DataPagamentoInicioFim InicioFim = converterData(dataInicioFim);
        OffsetDateTime inicio = InicioFim.getInicio();
        OffsetDateTime fim = InicioFim.getFim();
        List<com.jeanpiress.ProjetoBarbaria.domain.model.Pedido> pedidos = pedidoRepository.findByDataPagamento(inicio, fim);
        return pedidos;
    }

    private BigDecimal gerarTkmLoja(List<Pedido> pedidos, CaixaModel cd) {
        Integer quantidadePedidos = pedidos.size();
        BigDecimal faturamento = cd.getTotal();
        BigDecimal tkm = faturamento.divide(BigDecimal.valueOf(quantidadePedidos));

        return tkm;
    }

    private DataPagamentoInicioFim converterData(DataPagamentoJson dataInicioFim) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDateTime inicio = LocalDate.parse(dataInicioFim.getInicio(), formatter).atStartOfDay();
            LocalDateTime fim = LocalDate.parse(dataInicioFim.getFim(), formatter).atTime(23, 59, 59, 999999999);

            OffsetDateTime dataInicio = inicio.atOffset(ZoneOffset.UTC);
            OffsetDateTime dataFim = fim.atOffset(ZoneOffset.UTC);

            return DataPagamentoInicioFim.builder()
                    .inicio(dataInicio)
                    .fim(dataFim)
                    .build();
        }catch (DateTimeException e){
            throw new FormatoDataException("Formato de data incorreto, use: aaaa-mm-dd");
        }
    }
}
