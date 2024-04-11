package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.domain.Enuns.FormaPagamento;
import com.jeanpiress.ProjetoBarbaria.domain.model.CaixaDiario;
import com.jeanpiress.ProjetoBarbaria.domain.model.Pedido;
import com.jeanpiress.ProjetoBarbaria.domain.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;


@Service
public class CaixaDiarioService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private PedidoService pedidoService;

    public CaixaDiario gerarCaixa(){
        CaixaDiario caixaDiario;

        BigDecimal dinheiro = BigDecimal.ZERO;
        BigDecimal pix = BigDecimal.ZERO;
        BigDecimal credito = BigDecimal.ZERO;
        BigDecimal debito = BigDecimal.ZERO;
        BigDecimal voucher = BigDecimal.ZERO;
        BigDecimal pontos = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        List<Pedido> pedidos = pedidoRepository.findByPagoAndCaixaAberto();

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

        }

        total = total.add(dinheiro).add(pix).add(debito).add(credito);
        caixaDiario = CaixaDiario.builder()
                .dinheiro(dinheiro)
                .pix(pix)
                .debito(debito)
                .credito(credito)
                .voucher(voucher)
                .pontos(pontos)
                .total(total)
                .build();

        return caixaDiario;
    }


    public void fecharCaixa() {
        List<Pedido> pedidos = pedidoRepository.findByPagoAndCaixaAberto();
        pedidos.forEach(pedido -> pedido.setCaixaAberto(false));
        pedidoRepository.saveAll(pedidos);
        
    }
}
