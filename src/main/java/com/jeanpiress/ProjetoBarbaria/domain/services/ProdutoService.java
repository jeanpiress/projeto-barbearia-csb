package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.domain.eventos.ProdutoCriadoEvento;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.CategoriaNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ProdutoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import com.jeanpiress.ProjetoBarbaria.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProdutoService {

    private static final String MSG_PRODUTO_EM_USO = "Produto de código %d não pode ser removido, pois esta em uso";
    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ComissaoService comissaoService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public Produto buscarPorId(Long produtoId){
        return repository.findById(produtoId).
                orElseThrow(() -> new CategoriaNaoEncontradoException(produtoId));
    }
    @Transactional
    public Produto adicionar(Produto produto) {
        Long categoriaId = produto.getCategoria().getId();
        Categoria catagoria = categoriaService.buscarPorId(categoriaId);
        produto.setCategoria(catagoria);
        Produto produtoCriado = repository.save(produto);
        eventPublisher.publishEvent(new ProdutoCriadoEvento(produtoCriado));
        return produtoCriado;
    }

    @Transactional
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
}
