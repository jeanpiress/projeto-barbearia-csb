package com.jeanpiress.ProjetoBarbaria.repositories;

import com.jeanpiress.ProjetoBarbaria.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbaria.domain.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
}
