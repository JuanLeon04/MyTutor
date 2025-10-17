package uis.mytutor.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uis.mytutor.configuraciones.security.AesEncryptor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name=Usuario.TABLA_NAME)
public class Usuario {
    public static final String TABLA_NAME = "usuario";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @NotNull
    @Column(name = "correo", nullable = false)
    // @Convert(converter = AesEncryptor.class)
    private String correo;

    @NotNull
    @Column(name = "telefono", nullable = false)
    // @Convert(converter = AesEncryptor.class)
    private String telefono;

    @Column(name = "foto_perfil")
    // @Convert(converter = AesEncryptor.class)
    private String fotoPerfil;

    @Column(name = "activo")
    private boolean activo = true;

    @NotNull
    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    // constructores, getters y setters por lombok
}
