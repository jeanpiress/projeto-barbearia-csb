package com.jeanpiress.ProjetoBarbearia.domain.repositories;

import com.jeanpiress.ProjetoBarbearia.domain.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

    long count();
}
