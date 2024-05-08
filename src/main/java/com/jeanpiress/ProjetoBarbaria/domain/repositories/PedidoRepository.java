package com.jeanpiress.ProjetoBarbaria.domain.repositories;

import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p WHERE p.caixaAberto = true AND p.statusPagamento = 1")
    List<Pedido> findByPagoAndCaixaAberto();

    @Query("SELECT p FROM Pedido p WHERE p.dataPagamento > :inicio AND p.dataPagamento < :fim")
    List<Pedido> findByDataPagamento(@Param("inicio") OffsetDateTime inicio, @Param("fim") OffsetDateTime fim);

    @Query("SELECT p FROM Pedido p WHERE p.dataPagamento > :inicio AND p.dataPagamento < :fim AND p.profissional.id = :profissionalId")
    List<Pedido> findByDataPagamentoAndProfissionalId(@Param("inicio") OffsetDateTime inicio, @Param("fim") OffsetDateTime fim, @Param("profissionalId") Long profissionalId);


}
