package com.jeanpiress.ProjetoBarbaria.domain.repositories;

import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    @Query("SELECT p FROM Profissional p WHERE p.ativo = true ")
    Set<Profissional> buscarProfissionaisAtivos();

}
