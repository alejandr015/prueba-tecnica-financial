package com.financial.app.repository;

import com.financial.app.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByProductoId(Long productoId);
}