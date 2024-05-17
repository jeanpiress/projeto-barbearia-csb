package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.exceptions.PacoteNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.model.PacotePronto;
import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.PacoteProntoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class PacoteProntoService {

    @Autowired
    private PacoteProntoRepository pacoteProntoRepository;

    @Autowired
    private PacoteService pacoteService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProdutoService produtoService;


    public PacotePronto buscarPorId(Long pacoteProntoId) {
        return pacoteProntoRepository.findById(pacoteProntoId).
                orElseThrow(() -> new PacoteNaoEncontradoException(pacoteProntoId));

    }

    public List<PacotePronto> buscarPacotesAtivos() {
        return pacoteProntoRepository.buscarPacoteProntoAtivo();
    }


    public PacotePronto criarPacotePronto(PacotePronto pacotePronto) {
       pacotePronto.setItensAtivos(pacoteService.criarItensPacote(pacotePronto.getItensAtivos()));
        Categoria categoria = categoriaService.buscarPorId(pacotePronto.getCategoria().getId());
       Produto produto = Produto.builder()
               .nome(pacotePronto.getNome())
               .preco(calcularPrecoPacotePronto(pacotePronto))
               .pacotePronto(pacotePronto)
               .pesoPontuacaoCliente(pacotePronto.getPesoPontuacaoCliente())
               .pesoPontuacaoProfissional(pacotePronto.getPesoPontuacaoProfissional())
               .comissaoBase(pacotePronto.getComissaoBase())
               .categoria(categoria)
               .build();

       PacotePronto pacoteProntoSalvo = pacoteProntoRepository.save(pacotePronto);
        produtoService.adicionar(produto);
        return pacoteProntoSalvo;
    }

    private BigDecimal calcularPrecoPacotePronto(PacotePronto pacotePronto) {
        List<ItemPacote> itemPacotes = pacotePronto.getItensAtivos();
        BigDecimal valorPacotePronto = BigDecimal.ZERO;
        for(ItemPacote itemPacote : itemPacotes) {
            valorPacotePronto = valorPacotePronto.add(itemPacote.getItemPedido().getProduto().getPreco());
        }
        return valorPacotePronto;
    }


}
