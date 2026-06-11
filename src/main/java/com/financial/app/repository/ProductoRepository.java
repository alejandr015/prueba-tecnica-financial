package com.financial.app.repository;

import com.financial.app.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByClienteId(Long clienteId);
    boolean existsByClienteId(Long clienteId);
    Optional<Producto> findByNumeroCuenta(String numeroCuenta);
}