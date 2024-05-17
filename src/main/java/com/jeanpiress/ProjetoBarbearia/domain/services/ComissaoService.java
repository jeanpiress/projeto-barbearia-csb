package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.eventos.ProdutoCriadoEvento;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.ComissaoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbearia.domain.model.Comissao;
import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ComissaoRepository;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Autowired
    private ProfissionalRepository profissionalRepository;

    public Comissao buscarPorId(Long comissaoId){
        return repository.findById(comissaoId).
                orElseThrow(() -> new ComissaoNaoEncontradoException(comissaoId));
    }

    public Comissao adicionar(Comissao comissao) {
        Produto produto = produtoService.buscarPorId(comissao.getProduto().getId());
        Profissional profissional = profissionalService.buscarPorId(comissao.getProfissional().getId());
        comissao.setProduto(produto);
        comissao.setProfissional(profissional);
        return repository.save(comissao);
    }


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


    @EventListener
    public void criarComissaoBase(ProdutoCriadoEvento produtoEvento){
        Produto produto = produtoEvento.getProduto();
        Set<Profissional> profissionais = profissionalRepository.buscarProfissionaisAtivos();
        Comissao comissao = new Comissao();
        for(Profissional profissional: profissionais){
            comissao.setProduto(produto);
            comissao.setProfissional(profissional);
            comissao.setPorcentagemComissao(produto.getComissaoBase());

            repository.save(comissao);
        }
    }
}
