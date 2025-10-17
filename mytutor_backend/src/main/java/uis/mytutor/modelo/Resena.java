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

@Table(name=Resena.TABLA_NAME)
public class Resena {
    public static final String TABLA_NAME = "resena";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_autor", referencedColumnName = "id", nullable = false)
    private Usuario autor;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_receptor", referencedColumnName = "id", nullable = false)
    private Tutor receptor;

    @NotNull
    @Column(name = "puntuacion", nullable = false)
    private byte puntuacion;

    @Column(name = "comentario")
    private String comentario;

    // Asgina la fecha de creación del registro automáticamente
    @Column(name = "fecha", updatable = false)
    @CreationTimestamp
    private LocalDateTime fecha;

    // constructores, getters y setters por lombok
}
