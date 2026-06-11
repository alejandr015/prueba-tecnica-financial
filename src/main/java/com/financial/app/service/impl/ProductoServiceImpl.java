package com.financial.app.service.impl;

import com.financial.app.dto.ProductoDTO;
import com.financial.app.entity.Producto;
import com.financial.app.entity.Producto.EstadoCuenta;
import com.financial.app.entity.Producto.TipoCuenta;
import com.financial.app.exception.BusinessException;
import com.financial.app.repository.ClienteRepository;
import com.financial.app.repository.ProductoRepository;
import com.financial.app.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public Producto crear(ProductoDTO dto) {
        var cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        if (dto.getTipoCuenta() == TipoCuenta.AHORROS && dto.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("La cuenta de ahorros no puede tener saldo negativo");
        }

        String numeroCuenta = generarNumeroCuenta(dto.getTipoCuenta());

        Producto producto = Producto.builder()
                .tipoCuenta(dto.getTipoCuenta())
                .numeroCuenta(numeroCuenta)
                .estado(EstadoCuenta.ACTIVA)
                .saldo(dto.getSaldo())
                .exentaGMF(dto.getExentaGMF() != null ? dto.getExentaGMF() : false)
                .cliente(cliente)
                .build();

        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizarEstado(Long id, EstadoCuenta estado) {
        Producto producto = obtenerPorId(id);

        if (estado == EstadoCuenta.CANCELADA) {
            if (producto.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
                throw new BusinessException("Solo se puede cancelar una cuenta con saldo $0");
            }
        }

        producto.setEstado(estado);
        return productoRepository.save(producto);
    }

    @Override
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Producto no encontrado con id: " + id));
    }

    @Override
    public List<Producto> obtenerPorCliente(Long clienteId) {
        return productoRepository.findByClienteId(clienteId);
    }

    @Override
    public void cancelar(Long id) {
        actualizarEstado(id, EstadoCuenta.CANCELADA);
    }

    private String generarNumeroCuenta(TipoCuenta tipo) {
        String prefijo = tipo == TipoCuenta.AHORROS ? "53" : "33";
        String numeros;
        String numeroCuenta;
        do {
            numeros = String.format("%08d", new Random().nextInt(100000000));
            numeroCuenta = prefijo + numeros;
        } while (productoRepository.findByNumeroCuenta(numeroCuenta).isPresent());
        return numeroCuenta;
    }
}