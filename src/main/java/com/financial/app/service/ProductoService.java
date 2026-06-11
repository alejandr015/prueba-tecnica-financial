package com.financial.app.service;

import com.financial.app.dto.ProductoDTO;
import com.financial.app.entity.Producto;
import java.util.List;

public interface ProductoService {
    Producto crear(ProductoDTO dto);
    Producto actualizarEstado(Long id, Producto.EstadoCuenta estado);
    Producto obtenerPorId(Long id);
    List<Producto> obtenerPorCliente(Long clienteId);
    void cancelar(Long id);
}