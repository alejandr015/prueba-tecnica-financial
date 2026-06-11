package com.financial.app.controller;

import com.financial.app.dto.TransaccionDTO;
import com.financial.app.entity.Transaccion;
import com.financial.app.service.TransaccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    @PostMapping
    public ResponseEntity<Transaccion> realizar(@Valid @RequestBody TransaccionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionService.realizar(dto));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Transaccion>> obtenerPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(transaccionService.obtenerPorProducto(productoId));
    }
}