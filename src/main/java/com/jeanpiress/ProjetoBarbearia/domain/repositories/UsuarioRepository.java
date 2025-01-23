package com.jeanpiress.ProjetoBarbearia.domain.repositories;

import com.jeanpiress.ProjetoBarbearia.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) LIKE LOWER(CONCAT(:email, '%'))")
    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE 1 = 1 " +
            "AND (:profissionalId IS NULL OR u.profissional.id = :profissionalId) " +
            "AND (:clienteId IS NULL OR u.cliente.id = :clienteId)")
    Optional<Usuario> findByIdProfissionalCliente(Long profissionalId, Long clienteId);

    Boolean existsByEmail(String email);
}
