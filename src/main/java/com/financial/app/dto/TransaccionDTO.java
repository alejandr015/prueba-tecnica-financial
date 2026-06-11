package com.financial.app.dto;

import com.financial.app.entity.Transaccion.TipoTransaccion;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionDTO {

    @NotNull(message = "Tipo de transacción requerido")
    private TipoTransaccion tipo;

    @NotNull(message = "Monto requerido")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal monto;

    @NotNull(message = "Cuenta origen requerida")
    private Long productoId;

    private Long cuentaDestinoId;

    private String descripcion;
}