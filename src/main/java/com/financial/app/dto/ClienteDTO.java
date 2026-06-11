package com.financial.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {
    private Long id;

    @NotBlank(message = "Tipo de identificación requerido")
    private String tipoIdentificacion;

    @NotBlank(message = "Número de identificación requerido")
    private String numeroIdentificacion;

    @NotBlank(message = "Nombres requeridos")
    @Size(min = 2, message = "Nombre debe tener al menos 2 caracteres")
    private String nombres;

    @NotBlank(message = "Apellido requerido")
    @Size(min = 2, message = "Apellido debe tener al menos 2 caracteres")
    private String apellido;

    @Email(message = "Formato de correo inválido")
    @NotBlank(message = "Correo requerido")
    private String correoElectronico;

    @NotNull(message = "Fecha de nacimiento requerida")
    private LocalDate fechaNacimiento;
}