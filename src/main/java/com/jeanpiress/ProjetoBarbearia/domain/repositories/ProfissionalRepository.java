package com.jeanpiress.ProjetoBarbearia.domain.repositories;

import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    @Query("SELECT p FROM Profissional p WHERE p.ativo = true ")
    Set<Profissional> buscarProfissionaisAtivos();

}
