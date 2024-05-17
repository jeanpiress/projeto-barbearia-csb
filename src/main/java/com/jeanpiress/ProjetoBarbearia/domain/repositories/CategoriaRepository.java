package com.jeanpiress.ProjetoBarbearia.domain.repositories;

import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
