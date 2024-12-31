package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.CaixaModel;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.FaturamentoDia;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbearia.domain.model.Pedido;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CaixaService {

    @Autowired
    private PedidoRepository pedidoRepository;


    public CaixaModel gerarCaixa(List<Pedido> pedidos){
        CaixaModel caixaDiario;

        BigDecimal dinheiro = BigDecimal.ZERO;
        BigDecimal pix = BigDecimal.ZERO;
        BigDecimal credito = BigDecimal.ZERO;
        BigDecimal debito = BigDecimal.ZERO;
        BigDecimal voucher = BigDecimal.ZERO;
        BigDecimal pontos = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        int clientesAtendidos = 0;
        int produtosVendidos = 0;
        List<Long> clientesId = new ArrayList<>();
        FaturamentoDia faturamentoDia;
        List<FaturamentoDia> faturamentos = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            switch (pedido.getFormaPagamento()) {
                case DINHEIRO:
                    dinheiro = dinheiro.add(pedido.getValorTotal());
                    break;
                case PIX:
                    pix = pix.add(pedido.getValorTotal());
                    break;
                case CREDITO:
                    credito = credito.add(pedido.getValorTotal());
                    break;
                case DEBITO:
                    debito = debito.add(pedido.getValorTotal());
                    break;
                case VOUCHER:
                    voucher = voucher.add(pedido.getValorTotal());
                    break;
                case PONTO:
                    pontos = pontos.add(pedido.getValorTotal());
                    break;
            }

            Optional<FaturamentoDia> faturamentoEncontrado = faturamentos.stream().filter(faturamento ->
                    faturamento.getData().equals(pedido.getDataPagamento().toLocalDate())).findFirst();

            if(faturamentoEncontrado.isPresent()){
                faturamentoDia = faturamentoEncontrado.get();
                faturamentoDia.setTotal(faturamentoDia.getTotal().add(pedido.getValorTotal()));
                faturamentoDia.setClientesAtendidos(faturamentoDia.getClientesAtendidos() + 1);
            }else {
                faturamentoDia = FaturamentoDia.builder()
                        .data(pedido.getDataPagamento().toLocalDate())
                        .clientesAtendidos(1)
                        .total(pedido.getValorTotal())
                        .build();
                faturamentos.add(faturamentoDia);
            }

            if(!clientesId.contains(pedido.getCliente().getId())){
                clientesId.add(pedido.getCliente().getId());
                clientesAtendidos++;
            }

            for (ItemPedido itemPedido : pedido.getItemPedidos()) {
                produtosVendidos += itemPedido.getQuantidade();
            }

        }

        calcularTkm(faturamentos);

        total = total.add(dinheiro).add(pix).add(debito).add(credito);
        caixaDiario = CaixaModel.builder()
                .dinheiro(dinheiro)
                .pix(pix)
                .debito(debito)
                .credito(credito)
                .voucher(voucher)
                .pontos(pontos)
                .total(total)
                .clientesAtendidos(clientesAtendidos)
                .quantidadeProdutosVendidos(produtosVendidos)
                .faturamentos(faturamentos)
                .build();

        return caixaDiario;
    }

    private void calcularTkm(List<FaturamentoDia> faturamentos) {
         faturamentos.forEach(faturamento -> {
            if (faturamento.getClientesAtendidos() > 0) {
                faturamento.setTkm(faturamento.getTotal().divide(BigDecimal.valueOf(faturamento.getClientesAtendidos())));
            } else {
                faturamento.setTkm(BigDecimal.ZERO);
            }
        });
    }

    public CaixaModel gerarCaixaDiario(){
        List<Pedido> pedidos = pedidoRepository.findByEqualStatusAndIsCaixaAberto(true, StatusPagamento.PAGO);
        return gerarCaixa(pedidos);
    }

    public void fecharCaixa() {
        List<Pedido> pedidos = pedidoRepository.findByEqualStatusAndIsCaixaAberto(true, StatusPagamento.PAGO);
        pedidos.forEach(pedido -> pedido.setCaixaAberto(false));
        pedidoRepository.saveAll(pedidos);
        
    }


}
