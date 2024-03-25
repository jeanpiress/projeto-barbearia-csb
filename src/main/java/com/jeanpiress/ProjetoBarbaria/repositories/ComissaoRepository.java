package com.jeanpiress.ProjetoBarbaria.repositories;

import com.jeanpiress.ProjetoBarbaria.domain.model.Comissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComissaoRepository extends JpaRepository<Comissao, Long> {
}
