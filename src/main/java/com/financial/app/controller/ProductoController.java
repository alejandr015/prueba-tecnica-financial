package com.financial.app.controller;

import com.financial.app.dto.ProductoDTO;
import com.financial.app.entity.Producto;
import com.financial.app.entity.Producto.EstadoCuenta;
import com.financial.app.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Producto> actualizarEstado(@PathVariable Long id,
                                                     @RequestParam EstadoCuenta estado) {
        return ResponseEntity.ok(productoService.actualizarEstado(id, estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Producto>> obtenerPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(productoService.obtenerPorCliente(clienteId));
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        productoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}