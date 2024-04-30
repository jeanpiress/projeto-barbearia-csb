package com.jeanpiress.ProjetoBarbaria.domain.repositories;

import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(CONCAT(:nome, '%'))")
    List<Cliente> findByNome(String nome);


    @Query("SELECT c FROM Cliente c WHERE c.previsaoRetorno BETWEEN :dataInicial AND :dataFinal")
    List<Cliente> findByClientesRetornoEmDias(OffsetDateTime dataInicial, OffsetDateTime dataFinal);
}
