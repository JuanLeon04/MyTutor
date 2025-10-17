package uis.mytutor.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name=Reserva.TABLA_NAME)
public class Reserva {
    public static final String TABLA_NAME = "reserva";

    public enum Estado {
        PENDIENTE,
        CANCELADA,
        COMPLETADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_horario", referencedColumnName = "id", nullable = false)
    private Horario horario;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    @NotNull
    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.PENDIENTE;

    // Asgina la fecha de creación del registro automáticamente
    @Column(name = "fecha", updatable = false)
    @CreationTimestamp
    private LocalDateTime fecha;
}
