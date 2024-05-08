package com.jeanpiress.ProjetoBarbaria.domain.repositories;

import com.jeanpiress.ProjetoBarbaria.domain.model.Pacote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacoteRepository extends JpaRepository<Pacote, Long> {

    List<Pacote> findByClienteId(Long clienteId);

    boolean existsByIdAndItensAtivosId(Long pacoteId, Long itemPacoteId);

    @Query("SELECT p FROM Pacote p WHERE SIZE(p.itensAtivos) > 0")
    List<Pacote> findAllComItensAtivos();

    @Query("SELECT p FROM Pacote p WHERE SIZE(p.itensExpirados) > 0")
    List<Pacote> findAllComItensExpirados();
}
