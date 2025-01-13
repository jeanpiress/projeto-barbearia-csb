package com.jeanpiress.ProjetoBarbearia.domain.repositories;

import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) " +
            "LIKE LOWER(CONCAT(:nome, '%')) " +
            "AND p.ativo = :isAtivo " +
            "AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId)")
    List<Produto> findByNome(String nome, boolean isAtivo, Long categoriaId);

    @Query(value = "SELECT * FROM produto WHERE categoria_id = :id", nativeQuery = true)
    List<Produto> buscarPorCategoria(@Param("id") Long id);
}


