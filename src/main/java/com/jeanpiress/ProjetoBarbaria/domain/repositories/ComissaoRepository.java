package com.jeanpiress.ProjetoBarbaria.domain.repositories;

import com.jeanpiress.ProjetoBarbaria.domain.model.Comissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComissaoRepository extends JpaRepository<Comissao, Long> {

    @Query("SELECT c FROM Comissao c WHERE c.profissional.id = :profissionalId AND c.produto.id = :produtoId")
    Optional<Comissao> buscarPorProfissionalEProduto(@Param("profissionalId") Long profissionalId, @Param("produtoId") Long produtoId);
}
