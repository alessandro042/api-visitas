package mx.edu.cecyte.api_visitas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "visitantes")
public class Visitante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombreCompleto;

    @NotBlank(message = "El motivo de la visita es obligatorio")
    @Column(nullable = false)
    private String motivoVisita;

    @NotBlank(message = "La dirección MAC es obligatoria")
    @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$", message = "Formato de MAC Address inválido")
    @Column(nullable = false, unique = true)
    private String macAddress;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column
    private LocalDateTime fechaSalida;

    @PrePersist
    protected void onPrePersist() {
        this.fechaRegistro = LocalDateTime.now();
    }
}