package com.jeanpiress.ProjetoBarbearia.domain.repositories;

import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPagamento;
import com.jeanpiress.ProjetoBarbearia.domain.Enuns.StatusPedido;
import com.jeanpiress.ProjetoBarbearia.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query(value = "SELECT p FROM Pedido p WHERE p.caixaAberto = :isAberto " +
            "AND p.statusPagamento = :statusPagamento")
    List<Pedido> findByEqualStatusAndIsCaixaAberto(Boolean isAberto, StatusPagamento statusPagamento);


    @Query(value = "SELECT * FROM pedido p WHERE p.data_pagamento >= :inicio " +
            "AND p.data_pagamento <= :fim " +
            "AND p.status_pagamento = 'PAGO'", nativeQuery = true)
    List<Pedido> findByDataPagamento(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query(value = "SELECT * FROM pedido p WHERE p.data_pagamento > :inicio " +
            "AND p.data_pagamento < :fim " +
            "AND p.profissional_id = :profissionalId " +
            "AND p.status_pagamento = 'PAGO' ORDER BY p.data_pagamento", nativeQuery = true)
    List<Pedido> findByDataPagamentoAndProfissionalId(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim, @Param("profissionalId") Long profissionalId);


    @Query("SELECT p FROM Pedido p WHERE " +
            "(:statusPedido IS NULL OR p.statusPedido = :statusPedido) " +
            "AND p.statusPagamento = :statusPagamento")
    List<Pedido> findByStatus(StatusPedido statusPedido, StatusPagamento statusPagamento);


    @Query(value = "SELECT p FROM Pedido p WHERE p.horario >= :inicioDoDia AND p.horario < :fimDoDia " +
            "AND p.statusPedido <> :statusPedido AND p.isAgendamento = :isAgendamento")
    List<Pedido> findByDataExcetoStatus(OffsetDateTime inicioDoDia, OffsetDateTime fimDoDia, StatusPedido statusPedido, Boolean isAgendamento);


}
