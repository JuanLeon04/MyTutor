package uis.mytutor.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import uis.mytutor.modelo.Tutor;

import java.util.Optional;

public interface TutorRepositorio extends JpaRepository<Tutor, Long> {

    Optional<Tutor> findByUsuario_Id(Long idUsuario);
}
