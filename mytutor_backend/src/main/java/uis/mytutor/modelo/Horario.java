package uis.mytutor.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name=Horario.TABLA_NAME)
public class Horario {
    public static final String TABLA_NAME = "horario";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_tutor", referencedColumnName = "id", nullable = false)
    private Tutor tutor;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @NotNull
    @Column(name = "disponible", nullable = false)
    private boolean disponible = true;

    // constructores, getters y setters por lombok
}
