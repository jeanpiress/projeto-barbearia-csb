package com.jeanpiress.ProjetoBarbaria.domain.repositories;

import com.jeanpiress.ProjetoBarbaria.domain.model.Pacote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacoteRepository extends JpaRepository<Pacote, Long> {

    List<Pacote> findByClienteId(Long clienteId);

    boolean existsByIdAndItensAtivosId(Long pacoteId, Long itemPacoteId);
}
