package com.jeanpiress.ProjetoBarbaria.domain.repositories;

import com.jeanpiress.ProjetoBarbaria.domain.Enuns.StatusPagamento;
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

    StatusPagamento statusPago = StatusPagamento.PAGO;

    @Query(value = "SELECT * FROM pedido p WHERE p.caixa_aberto = true " +
            "AND p.status_pagamento = 1", nativeQuery = true)
    List<Pedido> findByPagoAndCaixaAberto();


    @Query(value = "SELECT * FROM pedido p WHERE p.data_pagamento > :inicio " +
            "AND p.data_pagamento < :fim " +
            "AND p.status_pagamento = 1", nativeQuery = true)
    List<Pedido> findByDataPagamento(@Param("inicio") OffsetDateTime inicio, @Param("fim") OffsetDateTime fim);

    @Query(value = "SELECT * FROM pedido p WHERE p.data_pagamento > :inicio " +
            "AND p.data_pagamento < :fim " +
            "AND p.profissional_id = :profissionalId " +
            "AND p.status_pagamento = 1", nativeQuery = true)
    List<Pedido> findByDataPagamentoAndProfissionalId(@Param("inicio") OffsetDateTime inicio, @Param("fim") OffsetDateTime fim, @Param("profissionalId") Long profissionalId);




}
