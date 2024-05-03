package com.jeanpiress.ProjetoBarbaria.domain.repositories;

import com.jeanpiress.ProjetoBarbaria.domain.model.PacotePronto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacoteProntoRepository extends JpaRepository<PacotePronto, Long> {

    @Query("SELECT p FROM PacotePronto p WHERE p.ativo = true")
    List<PacotePronto> buscarPacoteProntoAtivo();

}
