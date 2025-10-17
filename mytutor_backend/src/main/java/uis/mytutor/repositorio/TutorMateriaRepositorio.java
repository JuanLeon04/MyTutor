package uis.mytutor.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import uis.mytutor.modelo.TutorMateria;

public interface TutorMateriaRepositorio extends JpaRepository<TutorMateria, Long> {
}
