package com.financial.app.service.impl;

import com.financial.app.dto.ClienteDTO;
import com.financial.app.entity.Cliente;
import com.financial.app.exception.BusinessException;
import com.financial.app.repository.ClienteRepository;
import com.financial.app.repository.ProductoRepository;
import com.financial.app.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    @Override
    public Cliente crear(ClienteDTO dto) {
        validarEdad(dto.getFechaNacimiento());

        if (clienteRepository.existsByNumeroIdentificacion(dto.getNumeroIdentificacion())) {
            throw new BusinessException("Ya existe un cliente con ese número de identificación");
        }

        Cliente cliente = Cliente.builder()
                .tipoIdentificacion(dto.getTipoIdentificacion())
                .numeroIdentificacion(dto.getNumeroIdentificacion())
                .nombres(dto.getNombres())
                .apellido(dto.getApellido())
                .correoElectronico(dto.getCorreoElectronico())
                .fechaNacimiento(dto.getFechaNacimiento())
                .build();

        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizar(Long id, ClienteDTO dto) {
        Cliente cliente = obtenerPorId(id);
        validarEdad(dto.getFechaNacimiento());

        cliente.setTipoIdentificacion(dto.getTipoIdentificacion());
        cliente.setNumeroIdentificacion(dto.getNumeroIdentificacion());
        cliente.setNombres(dto.getNombres());
        cliente.setApellido(dto.getApellido());
        cliente.setCorreoElectronico(dto.getCorreoElectronico());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());

        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = obtenerPorId(id);

        if (productoRepository.existsByClienteId(id)) {
            throw new BusinessException("No se puede eliminar el cliente porque tiene productos vinculados");
        }

        clienteRepository.delete(cliente);
    }

    @Override
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado con id: " + id));
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    private void validarEdad(LocalDate fechaNacimiento) {
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < 18) {
            throw new BusinessException("El cliente debe ser mayor de edad");
        }
    }
}