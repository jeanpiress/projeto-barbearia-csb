package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.domain.eventos.ProdutoCriadoEvento;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.CategoriaNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ComissaoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.model.Comissao;
import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbaria.repositories.ComissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class ComissaoService {

    private static final String MSG_COMISSAO_EM_USO = "Comissao de código %d não pode ser removido, pois esta em uso";

    private static final String MSG_COMISSAO_NAO_ENCONTRADA = "Comissao não foi encontrada";
    @Autowired
    private ComissaoRepository repository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProfissionalService profissionalService;

    public Comissao buscarPorId(Long comissaoId){
        return repository.findById(comissaoId).
                orElseThrow(() -> new ComissaoNaoEncontradoException(comissaoId));
    }
    @Transactional
    public Comissao adicionar(Comissao comissao) {
        Produto produto = produtoService.buscarPorId(comissao.getProduto().getId());
        Profissional profissional = profissionalService.buscarPorId(comissao.getProfissional().getId());
        comissao.setProduto(produto);
        comissao.setProfissional(profissional);
        return repository.save(comissao);
    }

    @Transactional
    public void remover(Long comissaoId) {
        try {
            repository.deleteById(comissaoId);
        }catch (EmptyResultDataAccessException e) {
            throw new ComissaoNaoEncontradoException(comissaoId);
        }catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_COMISSAO_EM_USO, comissaoId));
        }
    }

    public Comissao buscarPorProfissionalProduto(Long profissionalId, Long produtoId){
        return repository.buscarPorProfissionalEProduto(profissionalId, produtoId).
                orElseThrow(() -> new ComissaoNaoEncontradoException(MSG_COMISSAO_NAO_ENCONTRADA));
    }

    public BigDecimal calculoComissaoProduto(Comissao comissao){
        BigDecimal valor_produto = comissao.getProduto().getPreco();
        BigDecimal porcentagem = comissao.getPorcentagemComissao();
        BigDecimal valorFinal = valor_produto.multiply(porcentagem).divide(new BigDecimal("100"));

        return valorFinal;
    }

    @Transactional
    @EventListener
    public void criarComissaoBase(ProdutoCriadoEvento produtoEvento){
        Produto produto = produtoEvento.getProduto();
        Set<Long> profissionaisIds = profissionalService.buscarIdProfissionaisAtivos();
        Comissao comissao = new Comissao();
        for(Long id: profissionaisIds){
            Profissional profissional = profissionalService.buscarPorId(id);
            comissao.setProduto(produto);
            comissao.setProfissional(profissional);
            comissao.setPorcentagemComissao(produto.getComissaoBase());

            repository.save(comissao);
        }
    }
}
