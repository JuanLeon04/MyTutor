package uis.mytutor.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import uis.mytutor.modelo.Usuario;

import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    // este método devuelve true si ya existe un usuario con ese nombre
    boolean existsByNombreUsuario(String nombreUsuario);
}
