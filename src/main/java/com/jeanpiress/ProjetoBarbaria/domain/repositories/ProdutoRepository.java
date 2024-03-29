package com.jeanpiress.ProjetoBarbaria.domain.repositories;

import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {


}
