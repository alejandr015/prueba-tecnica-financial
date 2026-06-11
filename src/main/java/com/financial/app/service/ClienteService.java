package com.financial.app.service;

import com.financial.app.dto.ClienteDTO;
import com.financial.app.entity.Cliente;
import java.util.List;

public interface ClienteService {
    Cliente crear(ClienteDTO dto);
    Cliente actualizar(Long id, ClienteDTO dto);
    void eliminar(Long id);
    Cliente obtenerPorId(Long id);
    List<Cliente> obtenerTodos();
}