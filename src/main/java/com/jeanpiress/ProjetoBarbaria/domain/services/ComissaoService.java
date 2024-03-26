package com.jeanpiress.ProjetoBarbaria.domain.services;

import com.jeanpiress.ProjetoBarbaria.domain.exceptions.CategoriaNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.ComissaoNaoEncontradoException;
import com.jeanpiress.ProjetoBarbaria.domain.exceptions.EntidadeEmUsoException;
import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.model.Comissao;
import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbaria.repositories.ComissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ComissaoService {

    private static final String MSG_COMISSAO_EM_USO = "Comissao de código %d não pode ser removido, pois esta em uso";
    @Autowired
    private ComissaoRepository repository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProfissionalService profissionalService;

    public Comissao buscarPorId(Long comissaoId){
        return repository.findById(comissaoId).
                orElseThrow(() -> new CategoriaNaoEncontradoException(comissaoId));
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
}
