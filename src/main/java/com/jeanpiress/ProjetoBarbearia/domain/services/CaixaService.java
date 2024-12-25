package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.CaixaModel;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbearia.domain.model.Pedido;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


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
        List<ItemPedido> itens = new ArrayList<>();

        for(Pedido pedido : pedidos){
            if(pedido.getFormaPagamento().equals(FormaPagamento.DINHEIRO)){
               dinheiro = dinheiro.add(pedido.getValorTotal());
            }
            if(pedido.getFormaPagamento().equals(FormaPagamento.PIX)){
                pix = pix.add(pedido.getValorTotal());
            }
            if(pedido.getFormaPagamento().equals(FormaPagamento.CREDITO)){
                credito = credito.add(pedido.getValorTotal());
            }
            if(pedido.getFormaPagamento().equals(FormaPagamento.DEBITO)){
                debito = debito.add(pedido.getValorTotal());
            }
            if(pedido.getFormaPagamento().equals(FormaPagamento.VOUCHER)){
                voucher = voucher.add(pedido.getValorTotal());
            }
            if(pedido.getFormaPagamento().equals(FormaPagamento.PONTO)){
                pontos = pontos.add(pedido.getValorTotal());
            }

            clientesAtendidos ++;

            itens = pedido.getItemPedidos();

            for(ItemPedido itemPedido : itens){
                produtosVendidos += itemPedido.getQuantidade();
            }

        }

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
                .build();

        return caixaDiario;
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
