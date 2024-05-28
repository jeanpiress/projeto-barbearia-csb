package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.eventos.PacoteProntoCriadoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.eventos.ProdutoCriadoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ProdutoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPacote;
import com.jeanpiress.ProjetoBarbearia.domain.model.PacotePronto;
import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {

    private static final String MSG_PRODUTO_EM_USO = "Produto de código %d não pode ser removido, pois esta em uso";
    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public Produto buscarPorId(Long produtoId){
        return repository.findById(produtoId).
                orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId));
    }

    public Produto adicionar(Produto produto) {
        Long categoriaId = produto.getCategoria().getId();
        Categoria catagoria = categoriaService.buscarPorId(categoriaId);
        produto.setCategoria(catagoria);
        Produto produtoCriado = repository.save(produto);
        eventPublisher.publishEvent(new ProdutoCriadoEvento(produtoCriado));
        return produtoCriado;
    }


    public void remover(Long produtoId) {
        try {
            repository.deleteById(produtoId);
        }catch (EmptyResultDataAccessException e) {
            throw new ProdutoNaoEncontradoException(produtoId);
        }catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_PRODUTO_EM_USO, produtoId));
        }
    }

    //Escutando de PacoteProntoService metodo criarPacotePronto
    @EventListener
    public void transformaPacoteProntoEmProdutoNovo(PacoteProntoCriadoEvento pacoteProntoEvento) {
        PacotePronto pacotePronto = pacoteProntoEvento.getPacotePronto();
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

        repository.save(produto);

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
