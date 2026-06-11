package com.financial.app.service;

import com.financial.app.dto.TransaccionDTO;
import com.financial.app.entity.Transaccion;
import java.util.List;

public interface TransaccionService {
    Transaccion realizar(TransaccionDTO dto);
    List<Transaccion> obtenerPorProducto(Long productoId);
}