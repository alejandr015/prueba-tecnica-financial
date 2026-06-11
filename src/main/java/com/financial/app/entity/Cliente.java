package com.financial.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String tipoIdentificacion;

    @NotBlank
    @Column(unique = true)
    private String numeroIdentificacion;

    @NotBlank
    @Size(min = 2)
    private String nombres;

    @NotBlank
    @Size(min = 2)
    private String apellido;

    @Email
    @NotBlank
    private String correoElectronico;

    @NotNull
    private LocalDate fechaNacimiento;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Producto> productos;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }
}