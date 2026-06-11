package com.financial.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipo;

    private BigDecimal monto;

    private LocalDateTime fecha;

    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    // Para transferencias: cuenta destino
    private Long cuentaDestinoId;

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
    }

    public enum TipoTransaccion {
        CONSIGNACION, RETIRO, TRANSFERENCIA
    }
}