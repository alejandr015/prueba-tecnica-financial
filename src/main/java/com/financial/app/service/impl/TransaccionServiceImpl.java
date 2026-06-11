package com.financial.app.service.impl;

import com.financial.app.dto.TransaccionDTO;
import com.financial.app.entity.Producto;
import com.financial.app.entity.Producto.EstadoCuenta;
import com.financial.app.entity.Transaccion;
import com.financial.app.entity.Transaccion.TipoTransaccion;
import com.financial.app.exception.BusinessException;
import com.financial.app.repository.ProductoRepository;
import com.financial.app.repository.TransaccionRepository;
import com.financial.app.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public Transaccion realizar(TransaccionDTO dto) {
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new BusinessException("Cuenta origen no encontrada"));

        if (producto.getEstado() != EstadoCuenta.ACTIVA) {
            throw new BusinessException("La cuenta no está activa");
        }

        switch (dto.getTipo()) {
            case CONSIGNACION -> realizarConsignacion(producto, dto.getMonto());
            case RETIRO -> realizarRetiro(producto, dto.getMonto());
            case TRANSFERENCIA -> realizarTransferencia(producto, dto.getMonto(), dto.getCuentaDestinoId());
        }

        Transaccion transaccion = Transaccion.builder()
                .tipo(dto.getTipo())
                .monto(dto.getMonto())
                .descripcion(dto.getDescripcion())
                .producto(producto)
                .cuentaDestinoId(dto.getCuentaDestinoId())
                .build();

        return transaccionRepository.save(transaccion);
    }

    @Override
    public List<Transaccion> obtenerPorProducto(Long productoId) {
        return transaccionRepository.findByProductoId(productoId);
    }

    private void realizarConsignacion(Producto producto, BigDecimal monto) {
        producto.setSaldo(producto.getSaldo().add(monto));
        productoRepository.save(producto);
    }

    private void realizarRetiro(Producto producto, BigDecimal monto) {
        if (producto.getTipoCuenta() == Producto.TipoCuenta.AHORROS &&
                producto.getSaldo().subtract(monto).compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Saldo insuficiente. La cuenta de ahorros no puede quedar en negativo");
        }
        producto.setSaldo(producto.getSaldo().subtract(monto));
        productoRepository.save(producto);
    }

    private void realizarTransferencia(Producto origen, BigDecimal monto, Long destinoId) {
        if (destinoId == null) {
            throw new BusinessException("Debe indicar la cuenta destino para la transferencia");
        }

        Producto destino = productoRepository.findById(destinoId)
                .orElseThrow(() -> new BusinessException("Cuenta destino no encontrada"));

        if (destino.getEstado() != EstadoCuenta.ACTIVA) {
            throw new BusinessException("La cuenta destino no está activa");
        }

        realizarRetiro(origen, monto);

        destino.setSaldo(destino.getSaldo().add(monto));
        productoRepository.save(destino);
    }
}