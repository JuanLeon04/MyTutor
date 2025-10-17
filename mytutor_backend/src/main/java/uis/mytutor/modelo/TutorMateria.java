package uis.mytutor.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name= TutorMateria.TABLA_NAME)
public class TutorMateria {
    public static final String TABLA_NAME = "tutor_materia";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_materia", referencedColumnName = "id", nullable = false)
    private Materia materia;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_tutor", referencedColumnName = "id", nullable = false)
    private Tutor tutor;

    @NotNull
    @Column(name = "nivel_experiencia", nullable = false)
    private byte nivelExperiencia;

    // constructores, getters y setters por lombok
}
