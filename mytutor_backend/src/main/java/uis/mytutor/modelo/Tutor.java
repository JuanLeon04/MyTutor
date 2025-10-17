package uis.mytutor.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name=Tutor.TABLA_NAME)
public class Tutor {
    public static final String TABLA_NAME = "tutor";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"activo", "nombreUsuario", "password"}) // Ignorar propiedades al serializar
    private Usuario usuario;

    @Column(name = "bio")
    private String bio;

    @Column(name = "precio_hora")
    private Double precioHora;

    @Column(name = "experiencia")
    private String experiencia;

    // constructores, getters y setters por lombok
}
