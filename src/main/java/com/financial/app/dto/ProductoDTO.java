package com.financial.app.dto;

import com.financial.app.entity.Producto.EstadoCuenta;
import com.financial.app.entity.Producto.TipoCuenta;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {
    private Long id;

    @NotNull(message = "Tipo de cuenta requerido")
    private TipoCuenta tipoCuenta;

    private String numeroCuenta;
    private EstadoCuenta estado;

    @NotNull(message = "Saldo requerido")
    private BigDecimal saldo;

    private Boolean exentaGMF;

    @NotNull(message = "Cliente requerido")
    private Long clienteId;
}